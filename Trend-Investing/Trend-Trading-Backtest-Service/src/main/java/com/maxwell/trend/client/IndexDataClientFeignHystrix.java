package com.maxwell.trend.client;

import cn.hutool.core.collection.CollectionUtil;
import com.maxwell.trend.pojo.IndexData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexDataClientFeignHystrix implements IndexDataClient {
    @Override
    public List<IndexData> getIndexData(String code) {
        return CollectionUtil.toList(new IndexData("0000-00-00", 0));
    }
}
