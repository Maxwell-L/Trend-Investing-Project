package com.maxwell.trend;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
@EnableDiscoveryClient
public class IndexConfigServerApplication {
    public static void main(String[] args) {
        int port = 8060;
        int eurekaServerPort = 8761;

        if(NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.println("启动本服务前请先启动 eureka 服务器");
            System.exit(1);
        }
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.println("端口 -" + port + "- 被占用, 无法启动");
        }
        new SpringApplicationBuilder(IndexConfigServerApplication.class)
                .properties("server.port=" + port)
                .run(args);
    }
}
