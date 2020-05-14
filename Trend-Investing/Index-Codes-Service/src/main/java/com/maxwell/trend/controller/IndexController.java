package com.maxwell.trend.controller;

import com.maxwell.trend.config.IpConfig;
import com.maxwell.trend.pojo.Index;
import com.maxwell.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    IndexService indexService;

    @Autowired
    IpConfig ipConfig;

    @GetMapping("/codes")
    @CrossOrigin
    public List<Index> codes() throws Exception {
        System.out.println("current instance's port is " + ipConfig.getPort());
        return indexService.get();
    }
}
