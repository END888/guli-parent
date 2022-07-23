package com.atguigu.guli.service.edu;

import com.atguigu.guli.service.base.result.R;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void test01() {
        // 添加
        stringRedisTemplate.opsForValue().set("username", "admin");
        // 判断是否包含
        System.out.println("stringRedisTemplate.hasKey(\"username\") = " + stringRedisTemplate.hasKey("username"));
        // 添加时设置过期时间
        stringRedisTemplate.opsForValue().set("username2", "root", 10, TimeUnit.MINUTES);
        System.out.println("stringRedisTemplate.getExpire(\"username2\",TimeUnit.MINUTES) = " + stringRedisTemplate.getExpire("username2", TimeUnit.MINUTES));
        //
        Gson gson = new Gson();
        stringRedisTemplate.opsForValue().set("strObj",gson.toJson(R.ok()));

        R r2 = gson.fromJson(stringRedisTemplate.opsForValue().get("strObj"), R.class);
        System.out.println(r2);
        System.out.println("--------------------------------------");
        // stringRedisTemplate 保存的对象如果是系统类对象，系统类已经实现了序列化接口
        // 存入的对象在redis中不可读
        // 如果我们希望 stringRedisTemplate 能够像 StringRedisTemplate 一样自动将对象转为json字符串自动存到redis，读取时自动将字符串转为java对象
        // stringRedisTemplate: 是以序列化的形式将对象存到redis，需要自定义类型的对象实现序列化接口
        redisTemplate.opsForValue().set("rtObj",R.error(),20,TimeUnit.MINUTES);
        Long obj = redisTemplate.getExpire("rtObj");
        System.out.println(obj);
        System.out.println(obj.getClass().getName());

    }
}
