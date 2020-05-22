package com.maxwell.trend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
public class TrendTradingBackTestViewApplication {
    public static void main(String[] args) {
        int port = 0;
        int defaultPort = 8041;
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
        if(0 == port) {
            Future<Integer> future = ThreadUtil.execAsync(() -> {
                int p = 0;
                System.out.println("请输入端口号, 否则使用默认端口" + defaultPort);
                Scanner in = new Scanner(System.in);
                while(true) {
                    String strPort = in.nextLine();
                    if(!NumberUtil.isInteger(strPort)) {
                        System.err.println("端口号必须是数字!");
                        continue;
                    } else {
                        p = Convert.toInt(strPort);
                        in.close();
                        break;
                    }
                }
                return p;
            });
            try {
                port = future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                port = defaultPort;
            }
        }
        // 判断 port 是否被占用
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.println("端口 -" + port + "- 被占用, 无法启动");
            System.exit(1);
        }

        new SpringApplicationBuilder(TrendTradingBackTestViewApplication.class)
                .properties("server.port=" + port)
                .run(args);
    }
}
