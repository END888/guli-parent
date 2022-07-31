package com.atguigu.guli.service.oss.schedule;

import com.atguigu.guli.service.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OSSTask {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    OssService ossService;

    @Scheduled(cron = "0 0 * * * ?")
    public void delTeacherAvatars() {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps("teacher:delfail:avatars");
        if (ops.size() == 0) {
            return;
        }
        ops.entries().forEach((path, module) -> {
            // 删除OSS的图片
            ossService.delete(path, module);
            // 将redis中的hash缓存删除
            ops.delete(path);
            log.info("redis定时任务：删除OSS图片执行。。。");
        });
    }
}
