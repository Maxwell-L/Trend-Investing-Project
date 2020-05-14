package com.maxwell.trend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.service.IndexDataService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "indexData")
public class IndexDataServiceImpl implements IndexDataService {
    @Override
    @Cacheable(key = "'indexData-code-' + #p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }
}
