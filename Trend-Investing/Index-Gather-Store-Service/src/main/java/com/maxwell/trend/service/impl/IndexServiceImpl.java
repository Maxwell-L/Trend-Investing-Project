package com.maxwell.trend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.maxwell.trend.pojo.Index;
import com.maxwell.trend.service.IndexService;
import com.maxwell.trend.util.SpringContextUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexServiceImpl implements IndexService {

    private List<Index> indexes;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @CacheEvict(allEntries = true)
    public void remove() {
        System.out.println("remove codes successfully");
    }

    @Override
    @Cacheable(key = "'allCodes'")
    public List<Index> store() {
        System.out.println(this);
        return indexes;
    }

    @Override
    @HystrixCommand(fallbackMethod = "thirdPartyNotConnected")
    public List<Index> fresh() {
        // 若可以获取到第三方数据则更新 否则remove(), store()操作不执行
        indexes = fetchIndexesFromThirdParty();
        IndexService indexService = SpringContextUtil.getBean(IndexService.class);
        indexService.remove();
        return indexService.store();
    }

    @Override
    @Cacheable(key = "'allCodes'")
    public List<Index> get() {
        return CollUtil.toList();
    }

    // 从第三方收集数据
    private List<Index> fetchIndexesFromThirdParty() {
        List<Map> maps = restTemplate.getForObject("http://localhost:8090/indexes/codes.json", List.class);
        return mapToIndex(maps);
    }

    private List<Index> thirdPartyNotConnected() {
        System.out.println("第三方数据服务连接失败...");
        return CollectionUtil.toList(new Index("000000", "无效指数代码 刷新失败"));
    }

    private List<Index> mapToIndex(List<Map> maps) {
        List<Index> indexes = new ArrayList<>();
        for(Map map : maps) {
            String code = map.get("code").toString();
            String name = map.get("name").toString();
            indexes.add(new Index(code, name));
        }
        return indexes;
    }
}
