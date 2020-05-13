package com.maxwell.trend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexData {
    private String date;
    private double closePoint;
}
