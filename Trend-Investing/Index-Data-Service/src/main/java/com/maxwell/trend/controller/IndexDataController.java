package com.maxwell.trend.controller;

import com.maxwell.trend.config.IpConfig;
import com.maxwell.trend.pojo.IndexData;
import com.maxwell.trend.service.IndexDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexDataController {
    @Autowired
    IndexDataService indexDataService;

    @Autowired
    IpConfig ipConfig;

    @GetMapping("/data/{code}")
    public List<IndexData> get(@PathVariable("code") String code) throws Exception {
        System.out.println("current instance is: " + ipConfig.getPort());
        return indexDataService.get(code);
    }
}
