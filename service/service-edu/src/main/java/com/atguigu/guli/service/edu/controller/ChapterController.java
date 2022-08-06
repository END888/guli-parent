package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/edu/chapter")
public class ChapterController {

    @Autowired
    ChapterService xxService;




    public R selectClasses(int id){

        return null;
    }
}

