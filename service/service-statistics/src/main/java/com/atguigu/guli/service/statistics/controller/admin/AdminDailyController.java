package com.atguigu.guli.service.statistics.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-06
 */
@RestController
@RequestMapping("/admin/statistics/daily")
public class AdminDailyController {

    @Autowired
    DailyService dailyService;

    // 1、生成指定日期的统计数据
    /**
     * 登录数量、注册数量、视频点播数量、课程发布数量：
     *  > 注册数量：只需要查询 UCenter 用户表，创建日期为指定日期的，就是当天的注册用户
     *  > 课程发布数量：查询课程发布时间等于指定日期的课程
     *  > 埋点：
     *      前后端都可以埋点，一般整合第三方的工具，在代码合适的地方调用第三方sdk的方法采集数据
     *  > 登录数量：redis（set：每次登录成功的用户的id存到redis的set中，在用户登录时将用户登录时的时间和ip等信息更新到日志表中）
     *  > 视频点播数量：在获取视频播放凭证时，将redis中存的统计数据持久化到数据库并删除缓存
     */
    @PostMapping("genDaily/{day}")
    public R genDaily(@PathVariable String day){
        dailyService.genDaily(day);
        return R.ok();
    }

    // 2、查询日期区间内的统计数据
    @GetMapping("getStatistics/{begin}/{end}")
    public R getStatistics(@PathVariable String begin, @PathVariable String end){
        // 将查询到的统计数据拆分到一个个的list集合中，最终存到一个map中
        /**
         * List<String> date: 时间日期集合
         * List<Integer> registerNums: 注册人数集合
         * List<Integer> loginNums: 登录人数集合
         * List<Integer> courseNums：客户才能发布数集合
         * List<Integer> videoViewNums: 视频浏览数集合
         * map.put("date",date)
         * map.put("registerNums",registerNums)
         */
        Map<String,Object> map = dailyService.getStatistics(begin,end);
        return R.ok().data(map);
    }

}

