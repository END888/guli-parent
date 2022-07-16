package com.atguigu.guli.service.edu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.atguigu.guli.service")
public class MainApplication {

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.druid")
//    public DruidDataSource druidDataSource(){
//        return new DruidDataSource();
//    }
}
