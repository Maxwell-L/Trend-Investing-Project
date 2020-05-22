package com.maxwell.trend.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.maxwell.trend.client.IndexDataClient;
import com.maxwell.trend.pojo.AnnualProfit;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.pojo.Profit;
import com.maxwell.trend.pojo.Trade;
import com.maxwell.trend.service.BackTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BackTestServiceImpl implements BackTestService {
    @Autowired
    IndexDataClient indexDataClient;

    @Override
    public List<IndexData> listIndexData(String code) {
        List<IndexData> result = indexDataClient.getIndexData(code);
        Collections.reverse(result);

//        for(IndexData indexData : result) {
//            System.out.println(indexData.getDate());
//        }
        return result;
    }

    @Override
    public Map<String, Object> simulate(int ma, double sellRate, double buyRate, double serviceCharge, List<IndexData> indexDataList) {
        List<Profit> profits = new ArrayList<>();
        List<Trade> trades = new ArrayList<>();
        double initCash = 1000;
        double cash = initCash;
        double share = 0;
        double value = 0;

        int winCount = 0;
        double totalWinRate = 0;
        double avgWinRate = 0;
        double totalLossRate= 0;
        int lossCount = 0;
        double avgLossRate = 0;

        double init = 0;
        if(!indexDataList.isEmpty()) {
            init = indexDataList.get(0).getClosePoint();
        }
        for(int i = 0; i < indexDataList.size(); i++) {
            IndexData indexData = indexDataList.get(i);
            double closePoint = indexData.getClosePoint();
            double avg = getMA(i, ma, indexDataList);
            double max = getMax(i, ma, indexDataList);
            double increaseRate = closePoint / avg;
            double decreaseRate = closePoint / max;
            if(avg != 0) {
                // 如果超过了均线
                if(increaseRate > buyRate) {
                    // 如果没买
                    if(0 == share) {
                        share = cash / closePoint;
                        cash = 0;

                        Trade trade = new Trade();
                        trade.setBuyDate(indexData.getDate());
                        trade.setSellDate("n/a");
                        trade.setBuyClosePoint(indexData.getClosePoint());
                        trade.setSellClosePoint(0);
                        trades.add(trade);
                    }
                } else if(decreaseRate < sellRate) { // sell 低于卖点
                    // 如果没卖
                    if(0 != share) {
                        cash = closePoint * share * (1 - serviceCharge);
                        share = 0;

                        Trade trade = trades.get(trades.size() - 1);
                        trade.setSellDate(indexData.getDate());
                        trade.setSellClosePoint(indexData.getClosePoint());
                        double rate = cash / initCash;
                        trade.setRate(rate);

                        if(trade.getSellClosePoint() - trade.getBuyClosePoint() > 0) {
                            totalWinRate += (trade.getSellClosePoint() - trade.getBuyClosePoint()) / trade.getBuyClosePoint();
                            winCount++;
                        } else {
                            totalLossRate += (trade.getSellClosePoint() - trade.getBuyClosePoint()) / trade.getBuyClosePoint();
                            lossCount++;
                        }
                    }
                }
            }
            if(share != 0) {
                value = closePoint * share;
            } else {
                value = cash;
            }
            double rate = value / initCash;
            Profit profit = new Profit(indexData.getDate(), rate * init);

            System.out.println("profit.value:" + profit.getValue());
            profits.add(profit);
        }

        avgWinRate = totalWinRate / winCount;
        avgLossRate = totalLossRate / lossCount;

        List<AnnualProfit> annualProfits = calculateAnnualProfits(indexDataList, profits);

        Map<String, Object> map = new HashMap<>();
        map.put("profits", profits);
        map.put("trades", trades);
        map.put("winCount", winCount);
        map.put("lossCount", lossCount);
        map.put("avgWinRate", avgWinRate);
        map.put("avgLossRate", avgLossRate);
        map.put("annualProfits", annualProfits);
        return map;
    }

    private static double getMax(int i, int day, List<IndexData> indexDataList) {
        int start = i - day - 1;
        if(start < 0) {
            start = 0;
        }
        int now = i - 1;
        double max = 0;
        for(int j = start; j < now; j++) {
            IndexData bean = indexDataList.get(j);
            if(bean.getClosePoint() > max) {
                max = bean.getClosePoint();
            }
        }
        return max;
    }

    private static double getMA(int i, int ma, List<IndexData> indexDataList) {
        int start = i - ma - 1;
        int now = i - 1;
        if(start < 0) {
            return 0;
        }
        double sum = 0;
        double avg = 0;
        for(int j = start; j < now; j++) {
            IndexData bean = indexDataList.get(j);
            sum += bean.getClosePoint();
        }
        avg = sum / (now - start);
        return avg;
    }

    @Override
    public double getYear(List<IndexData> allIndexData) {
        double year;
        String strDateStart = allIndexData.get(0).getDate();
        String strDateEnd = allIndexData.get(allIndexData.size() - 1).getDate();
        Date dateStart = DateUtil.parse(strDateStart);
        Date dateEnd = DateUtil.parse(strDateEnd);
        long days = DateUtil.between(dateStart, dateEnd, DateUnit.DAY);
        year = days / 365.0;
        return year;
    }

    @Override
    public int getYear(String date) {
        String strYear = StrUtil.subBefore(date, "-", false);
        return Convert.toInt(strYear);
    }

    @Override
    public double getIndexIncome(int year, List<IndexData> indexDataList) {
        IndexData first = null;
        IndexData last = null;
        for(IndexData indexData : indexDataList) {
            String strDate = indexData.getDate();
            int currentYear = getYear(strDate);
            if(currentYear == year) {
                if(null == first) {
                    first = indexData;
                }
                last = indexData;
            }
        }
        return (last.getClosePoint() - first.getClosePoint()) / first.getClosePoint();
    }

    @Override
    public double getTrendIncome(int year, List<Profit> profits) {
        Profit first = null;
        Profit last = null;
        for(Profit profit : profits) {
            String strDate = profit.getDate();
            int currentYear = getYear(strDate);
            if(currentYear == year) {
                if(null == first) {
                    first = profit;
                }
                last = profit;
            }
            if(currentYear > year) {
                break;
            }
        }
        return (last.getValue() - first.getValue()) / first.getValue();
    }

    @Override
    public List<AnnualProfit> calculateAnnualProfits(List<IndexData> indexDataList, List<Profit> profits) {
        List<AnnualProfit> result = new ArrayList<>();
        String strStartDate = indexDataList.get(0).getDate();
        String strEndDate = indexDataList.get(indexDataList.size() - 1).getDate();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);
        int startYear = DateUtil.year(startDate);
        int endYear = DateUtil.year(endDate);
        for(int year= startYear; year <= endYear; year++) {
            AnnualProfit annualProfit = new AnnualProfit();
            annualProfit.setYear(year);
            double indexIncome = getIndexIncome(year, indexDataList);
            double trendIncome = getTrendIncome(year, profits);
            annualProfit.setIndexIncome(indexIncome);
            annualProfit.setTrendIncome(trendIncome);
            result.add(annualProfit);
        }
        return result;
    }
}
