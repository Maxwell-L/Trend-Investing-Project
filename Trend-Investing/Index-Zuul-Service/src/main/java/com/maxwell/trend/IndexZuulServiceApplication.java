package com.maxwell.trend;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient
public class IndexZuulServiceApplication {
    public static void main(String[] args) {
        int port = 8031;
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.println("端口 -" + port + "- 被占用, 无法启动");
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexZuulServiceApplication.class)
                .properties("server.port=" + port)
                .run(args);
    }
}
