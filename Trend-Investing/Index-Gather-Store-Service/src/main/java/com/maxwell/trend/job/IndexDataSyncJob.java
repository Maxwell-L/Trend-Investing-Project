package com.maxwell.trend.job;

import cn.hutool.core.date.DateUtil;
import com.maxwell.trend.pojo.Index;
import com.maxwell.trend.service.IndexDataService;
import com.maxwell.trend.service.IndexService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class IndexDataSyncJob extends QuartzJobBean {
    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexDataService indexDataService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时刷新启动: " + DateUtil.now());
        List<Index> indexes = indexService.fresh();
        for(Index index : indexes) {
            indexDataService.fresh(index.getCode());
        }
        System.out.println("定时刷新结束: " + DateUtil.now());
    }
}
