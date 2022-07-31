package com.atguigu.guli.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CrossOriginConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);// 是否允许携带cookie
        config.addAllowedHeader("*");// 允许的请求头
        config.addAllowedMethod("*");// 允许的请求方法
        config.addAllowedOrigin("*");// 允许的请求源
        configSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(configSource);
    }
}
