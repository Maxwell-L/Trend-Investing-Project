package com.maxwell.trend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.maxwell.trend.pojo.Index;
import com.maxwell.trend.service.IndexService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexServiceImpl implements IndexService {

    private List<Index> indexes;

    @Override
    @Cacheable(key = "'allCodes'")
    public List<Index> get() {
        return CollUtil.toList();
    }
}
