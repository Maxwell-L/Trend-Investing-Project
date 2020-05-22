package com.maxwell.trend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualProfit {
    private int year;
    private double indexIncome;
    private double trendIncome;

    @Override
    public String toString() {
        return "AnnualProfit [year=" + year +
                ", indexIncome=" + indexIncome +
                ", trendIncome=" + trendIncome +
                "]";
    }
}
