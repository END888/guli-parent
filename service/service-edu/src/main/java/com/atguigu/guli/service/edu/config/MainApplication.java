package com.atguigu.guli.service.edu.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.atguigu.guli.service")
@EnableTransactionManagement
public class MainApplication {

    @Bean
    Logger.Level level(){
        return Logger.Level.FULL;
    }
}
