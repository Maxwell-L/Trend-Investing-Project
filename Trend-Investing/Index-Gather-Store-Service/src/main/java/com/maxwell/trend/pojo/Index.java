package com.maxwell.trend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Index implements Serializable {
    private String code;
    private String name;
}
