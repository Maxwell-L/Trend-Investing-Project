package com.maxwell.trend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.service.IndexDataService;
import com.maxwell.trend.util.SpringContextUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "indexData")
public class IndexDataServiceImpl implements IndexDataService {
    private Map<String, List<IndexData>> indexDataMap = new HashMap<>();

    @Autowired
    RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "thirdPartyNotConnected")
    public List<IndexData> fresh(String code) {
        List<IndexData> indexData = fetchIndexesFromThirdParty(code);
        indexDataMap.put(code, indexData);
        System.out.println("code:" + code + "indexesData:" + indexDataMap.get(code).size());

        IndexDataService indexDataService = SpringContextUtil.getBean(IndexDataService.class);
        indexDataService.remove(code);
        return indexDataService.store(code);
    }

    @Override
    @CachePut(key = "'indexData-code-' + #p0")
    public List<IndexData> store(String code) {
        return indexDataMap.get(code);
    }

    @Override
    @Cacheable(key = "'indexData-code-' + #p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }

    @Override
    @CacheEvict(key = "'indexData-code-' + #p0")
    public void remove(String code) {

    }

    // 从第三方收集数据
    private List<IndexData> fetchIndexesFromThirdParty(String code) {
        List<Map> maps = restTemplate.getForObject("http://localhost:8090/indexes/" + code + ".json", List.class);
        return mapToIndex(maps);
    }

    private List<IndexData> thirdPartyNotConnected(String code) {
        System.out.println("第三方数据服务连接失败...");
        return CollectionUtil.toList(new IndexData("N/A", 0));
    }

    private List<IndexData> mapToIndex(List<Map> maps) {
        List<IndexData> indexData = new ArrayList<>();
        for(Map map : maps) {
            String date = map.get("date").toString();
            double closePoint = Convert.toDouble(map.get("closePoint"));
            indexData.add(new IndexData(date, closePoint));
        }
        return indexData;
    }
}
