package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feigin.EduFeignClient;
import com.atguigu.guli.service.statistics.feigin.UCenterFeignClient;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-06
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    EduFeignClient eduFeignClient;
    @Autowired
    UCenterFeignClient ucenterFeignClient;

    @Override
    public void genDaily(String day) {
        long count = this.count(new LambdaQueryWrapper<Daily>().eq(Daily::getDateCalculated, day));
        if (count > 0){
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
        }
        R coursePublishNumR = eduFeignClient.getCoursePublishNum(day);
        if (coursePublishNumR.getCode() != 20000){
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
        }
        R registerNumR = ucenterFeignClient.getRegisterNum(day);
        if (registerNumR.getCode() != 20000){
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
        }
        Integer coursePublishNum = Integer.valueOf(coursePublishNumR.getData().get("num").toString());
        Integer registerNum = Integer.valueOf(registerNumR.getData().get("num").toString());
        // 为了避免同一天生成多次统计数据：可以做日期的唯一验证
        // 根据day日期获取需要的数据，持久化到数据库统计表中
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerNum);
        daily.setCourseNum(coursePublishNum);

        daily.setLoginNum(RandomUtils.nextInt(800,2000));
        daily.setVideoViewNum(RandomUtils.nextInt(2000,10000));
        this.save(daily);
    }

    @Override
    public Map<String, Object> getStatistics(String begin, String end) {
        List<Daily> dailies = this.list(new LambdaQueryWrapper<Daily>()
                .ge(Daily::getDateCalculated, begin)
                .le(Daily::getDateCalculated, end)
                .orderByAsc(Daily::getDateCalculated));
        // 解析数据：
        // 日期集合
        List<String> date = dailies.stream().map(Daily::getDateCalculated).collect(Collectors.toList());
        List<Integer> registerNums = dailies.stream().map(Daily::getRegisterNum).collect(Collectors.toList());
        List<Integer> courseNums = dailies.stream().map(Daily::getCourseNum).collect(Collectors.toList());
        List<Integer> loginNums = dailies.stream().map(Daily::getLoginNum).collect(Collectors.toList());
        List<Integer> videoViewNums = dailies.stream().map(Daily::getVideoViewNum).collect(Collectors.toList());

        HashMap<String, Object> map = new HashMap<>();
        map.put("date",date);
        map.put("registerNums",registerNums);
        map.put("courseNums",courseNums);
        map.put("loginNums",loginNums);
        map.put("videoViewNums",videoViewNums);
        return map;
    }
}
