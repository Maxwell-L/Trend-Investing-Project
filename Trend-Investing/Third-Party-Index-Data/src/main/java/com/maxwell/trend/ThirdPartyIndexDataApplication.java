package com.maxwell.trend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ThirdPartyIndexDataApplication {
    public static void main(String[] args) {
        int port = 8090;
        int eurekaServerPort = 8761;

        // 判断 eureka-server 是否启动
        if(NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.println("启动本服务前请先启动 eureka 服务器");
            System.exit(1);
        }
        // 若启动时带参数, 可将该服务端口改为所带参数
        if(null != args && 0 != args.length) {
            for(String arg : args) {
                if(arg.startsWith("port=")) {
                    String strPort = StrUtil.subAfter(arg, "port=", true);
                    if(NumberUtil.isNumber(strPort)) {
                        port = Convert.toInt(strPort);
                    }
                }
            }
        }
        // 判断 port 是否被占用
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.println("端口 -" + port + "- 被占用, 无法启动");
            System.exit(1);
        }
        // 启动
        new SpringApplicationBuilder(ThirdPartyIndexDataApplication.class)
                .properties("server.port = " + port)
                .run(args);
    }
}
