package com.atguigu.guli.service.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.atguigu.guli.service"})
@EnableFeignClients
public class ServiceStatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceStatisticsApplication.class,args);
    }
}
