package com.atguigu.guli.service.edu.feign.impl;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test() {
        return R.error();
    }

    /**
     *  日志保存：以后一般需要手动解决问题
     *  方案：
     *      1、mq：不希望有较大延迟的场景
     *          - 删除失败的兜底方法，发送消息到mq的消息队列
     *          - 消息队列的消费者获取消息中的删除失败的头像地址再次尝试删除，如果失败重试后仍失败存到死信队列
     *      2、redis：适合数据缓存，数据消费由我们自己编程控制
     *          - List： lpush、rpop、rpush、lpop
     *          - 删除失败保存删除失败的信息到redis的list中
     *          - 创建一个定时任务，每过1小时获取list中的消息消费，遍历再次尝试删除消息
     */
    @Override
    public R remove(String url) {
        // 保存删除失败的头像地址和它的模块
        log.warn("ossClient远程删除讲师头像失败，地址：{}",url);
        return R.error().message("阿里云头像删除失败");
    }

    @Override
    public R test2(String str) {
        log.warn("熔断保护");
        return null;
    }

    @Override
    public R test3(R r) {
        return null;
    }

    @Override
    public String entity(BaseEntity baseEntity) {
        return null;
    }
}
