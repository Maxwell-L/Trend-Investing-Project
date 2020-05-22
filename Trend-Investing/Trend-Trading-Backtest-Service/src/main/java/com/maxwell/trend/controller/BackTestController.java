package com.maxwell.trend.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.maxwell.trend.pojo.AnnualProfit;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.pojo.Profit;
import com.maxwell.trend.pojo.Trade;
import com.maxwell.trend.service.BackTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BackTestController {
    @Autowired
    BackTestService backTestService;

    @GetMapping("/simulate/{code}/{ma}/{buyThreshold}/{sellThreshold}/{serviceCharge}/{startDate}/{endDate}")
    @CrossOrigin
    public Map<String, Object> backTest(@PathVariable("code") String code,
                                        @PathVariable("ma") int ma,
                                        @PathVariable("buyThreshold") double buyThreshold,
                                        @PathVariable("sellThreshold") double sellThreshold,
                                        @PathVariable("serviceCharge") double serviceCharge,
                                        @PathVariable("startDate") String strStartDate,
                                        @PathVariable("endDate") String strEndDate) throws Exception {
        List<IndexData> allIndexData = backTestService.listIndexData(code);

        String indexStartDate = allIndexData.get(0).getDate();
        String indexEndDate = allIndexData.get(allIndexData.size() - 1).getDate();

        allIndexData = filterByDateRange(allIndexData, strStartDate, strEndDate);

        double sellRate = sellThreshold;
        double buyRate = buyThreshold;
        Map<String, ?> simulateResult = backTestService.simulate(ma, sellRate, buyRate, serviceCharge, allIndexData);
        List<Profit> profits = (List<Profit>)simulateResult.get("profits");
        List<Trade> trades = (List<Trade>)simulateResult.get("trades");

        double year = backTestService.getYear(allIndexData);
        double indexIncomeTotal =
                (allIndexData.get(allIndexData.size() - 1).getClosePoint() - allIndexData.get(0).getClosePoint()) /
                allIndexData.get(0).getClosePoint();
        double indexIncomeAnnual = Math.pow(1 + indexIncomeTotal, 1 / year) - 1;
        double trendIncomeTotal =
                (profits.get(profits.size() - 1).getValue() - profits.get(0).getValue()) /
                profits.get(0).getValue();
        double trendIncomeAnnual = Math.pow(1 + trendIncomeTotal, 1 / year) - 1;

        int winCount = (Integer)simulateResult.get("winCount");
        int lossCount = (Integer)simulateResult.get("lossCount");
        double avgWinRate = (Double)simulateResult.get("avgWinRate");
        double avgLossRate = (Double)simulateResult.get("avgLossRate");

        List<AnnualProfit> annualProfits = (List<AnnualProfit>)simulateResult.get("annualProfits");

        Map<String, Object> result = new HashMap<>();
        result.put("indexData", allIndexData);
        result.put("indexStartDate", indexStartDate);
        result.put("indexEndDate", indexEndDate);
        result.put("profits", profits);
        result.put("trades", trades);
        result.put("year", year);
        result.put("indexIncomeTotal", indexIncomeTotal);
        result.put("indexIncomeAnnual", indexIncomeAnnual);
        result.put("trendIncomeTotal", trendIncomeTotal);
        result.put("trendIncomeAnnual", trendIncomeAnnual);
        result.put("winCount", winCount);
        result.put("lossCount", lossCount);
        result.put("avgWinRate", avgWinRate);
        result.put("avgLossRate", avgLossRate);
        result.put("annualProfits", annualProfits);
        return result;
    }

    private List<IndexData> filterByDateRange(List<IndexData> allIndexData, String strStartDate, String strEndDate) {
        if(StrUtil.isBlankOrUndefined(strStartDate) || StrUtil.isBlankOrUndefined(strEndDate)) {
            return allIndexData;
        }
        List<IndexData> result = new ArrayList<>();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);

        for(IndexData indexData: allIndexData) {
            Date date = DateUtil.parse(indexData.getDate());
            if(date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
                result.add(indexData);
            }
        }
        return result;
    }
}
