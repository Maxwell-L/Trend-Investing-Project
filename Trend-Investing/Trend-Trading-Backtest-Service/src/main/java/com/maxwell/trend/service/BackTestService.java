package com.maxwell.trend.service;

import com.maxwell.trend.pojo.AnnualProfit;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.pojo.Profit;

import java.util.List;
import java.util.Map;

public interface BackTestService {
    List<IndexData> listIndexData(String code);

    Map<String, Object> simulate(int ma, double sellRate, double buyRate, double serviceCharge, List<IndexData> indexDataList);

    double getYear(List<IndexData> allIndexData);

    int getYear(String date);

    double getIndexIncome(int year, List<IndexData> indexDataList);

    double getTrendIncome(int year, List<Profit> profits);

    List<AnnualProfit> calculateAnnualProfits(List<IndexData> indexDataList, List<Profit> profits);
}
