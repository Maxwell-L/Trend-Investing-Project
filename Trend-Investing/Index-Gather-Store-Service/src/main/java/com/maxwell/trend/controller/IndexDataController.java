package com.maxwell.trend.controller;

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

    @GetMapping("/freshIndexData/{code}")
    public List<IndexData> fresh(@PathVariable("code") String code) {
        return indexDataService.fresh(code);
    }

    @GetMapping("/getIndexData/{code}")
    public List<IndexData> get(@PathVariable("code") String code ) {
        return indexDataService.get(code);
    }

    @GetMapping("/removeIndexData/{code}")
    public String remove(@PathVariable("code") String code) {
        indexDataService.remove(code);
        return "remove " + code + " successfully";
    }
}
