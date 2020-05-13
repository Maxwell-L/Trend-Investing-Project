package com.maxwell.trend.service;

import com.maxwell.trend.pojo.IndexData;

import java.util.List;

public interface IndexDataService {

    List<IndexData> fresh(String code);

    List<IndexData> store(String code);

    List<IndexData> get(String code);

    void remove(String code);
}
