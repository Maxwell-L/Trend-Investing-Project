package com.maxwell.trend;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        // 默认端口号 8761
        int port = 8761;
        // 进行端口号判断
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了 无法启动%n", port);
            System.exit(1);
        }
        new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port = " + port).run(args);
    }
}
