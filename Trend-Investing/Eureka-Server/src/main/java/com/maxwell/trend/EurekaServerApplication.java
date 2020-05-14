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
            System.err.println("端口 -" + port + "- 被占用, 无法启动");
            System.exit(1);
        }
        new SpringApplicationBuilder(EurekaServerApplication.class)
                .properties("server.port=" + port)
                .run(args);
    }
}
