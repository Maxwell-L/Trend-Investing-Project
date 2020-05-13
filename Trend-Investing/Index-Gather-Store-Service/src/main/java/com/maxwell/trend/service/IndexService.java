package com.maxwell.trend.service;

import com.maxwell.trend.pojo.Index;

import java.util.List;

public interface IndexService {

    void remove();

    List<Index> fresh();

    List<Index> store();

    List<Index> get();

}
