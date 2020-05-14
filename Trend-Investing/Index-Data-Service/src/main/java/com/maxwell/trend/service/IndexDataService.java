package com.maxwell.trend.service;

import com.maxwell.trend.pojo.IndexData;

import java.util.List;

public interface IndexDataService {
    List<IndexData> get(String code);
}
