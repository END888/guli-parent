package com.atguigu.guli.service.edu.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

//@Component
@Slf4j
public class MyJobTask {

    // 每过5秒执行一次定时任务
    // * 代表所有
    // */5 代表每过5秒执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    public void info(){
        log.info("定时任务执行。。。当前时间：{}",new Date());
    }
}
