package com.maxwell.trend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private String buyDate;
    private String sellDate;
    private double buyClosePoint;
    private double sellClosePoint;
    private double rate;
}
