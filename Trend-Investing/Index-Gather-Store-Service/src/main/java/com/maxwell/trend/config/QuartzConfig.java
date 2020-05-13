package com.maxwell.trend.config;

import com.maxwell.trend.job.IndexDataSyncJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static final int INTERVAL = 4;

    @Bean
    public JobDetail weatherDataSyncJobDetail() {
        return JobBuilder.newJob(IndexDataSyncJob.class)
                .withIdentity("indexDataSyncJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weatherDataSyncTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(INTERVAL)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(weatherDataSyncJobDetail())
                .withIdentity("indexDataSyncTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
