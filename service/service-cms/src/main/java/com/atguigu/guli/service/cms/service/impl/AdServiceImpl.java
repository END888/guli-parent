package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.feign.EduFeignClient;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    EduFeignClient eduFeignClient;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * aop:
     *      每个需要缓存管理的业务方法，步骤如下：
     *          先查询缓存，有数据则返回
     *          没有数据，查询数据库，存到缓存中返回
     *      SpringCaching 基于 aop 实现了缓存管理：
     *          使用步骤：[老师的]
     *              1、需要按照 SpringCaching 提供 CacheManager 接口编写实现类（提供缓存管理的业务）
     *                  SpringBoot-redis 启动器已经提供了缓存管理的实现类
     *              2、需要在启动类上添加注解启用 SpringCaching 的缓存管理功能
     *              3、在需要缓存管理的查询方法添加缓存管理的注解（指定缓存的key）
     *          使用步骤：【我总结的】
     *              1、引入 redis 的数据库连接池：commons-pool2
     *              2、配置 redis 连接池
     *              3、在配置类中给 RedisTemplate 设置键和值的序列化器
     *              4、在配置类中初始化 redisCacheManager 对象注入到容器中
     *              5、启动类上添加 @EnableCaching //启用自动缓存管理
     *              6、在要进行 redis 缓存的方法上添加 @Cacheable(value = "ads",key = "'cache'")
     *
     *  value+key 组合形成 redis 中缓存的键，拼接后的规则：value::key
     * @Cacheable(value = "ads",key="'cache'")// key 的值，必须使用单引号引起
     *
     */
    @Cacheable(value = "ads",key = "'cache'")
    @Override
    public Map<String, List> getHosts() {
//        //先判断 redis 中是否存在缓存
//        Object cacheObj = redisTemplate.opsForValue().get("ads::cache");
//        if (cacheObj!=null){
//            return (Map<String, List>) cacheObj;
//        }
        // 没有缓存查数据库
        Map<String,List> map = new HashMap<>();
        List<Ad> ads = this.list();
        map.put("banners",ads);
        // 查询热门课程
        R rHotCourses = eduFeignClient.getHotCourses();
        // 查询热门讲师
        R rHotTeachers = eduFeignClient.getHotTeachers();
        if (!rHotCourses.getSuccess() || !rHotTeachers.getSuccess()){
            throw new GuliException(ResultCodeEnum.SERVER_INNER_ERROR);
        }
        map.put("courses", (List) rHotCourses.getData().get("items"));
        map.put("teachers", (List) rHotTeachers.getData().get("items"));
//        // 将查询到的数据缓存到redis中,并设置过期时间，可以让缓存失效
//        redisTemplate.opsForValue().set("ads::cache",map,200, TimeUnit.MILLISECONDS);
        return map;
    }
}
