package com.atguigu.guli.service.edu.controller.api;


//import com.atguigu.guli.service.base.exception.GuliException;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.atguigu.guli.service.edu.vo.SubjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Api(tags = "课程类别管理")
@RestController
@RequestMapping("/api/edu/subject")
@Slf4j
public class ApiSubjectController {
    @Autowired
    private SubjectService subjectService;


    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVoList = subjectService.nestedList2();
        return R.ok().data("items",subjectVoList);
    }


    @ApiOperation(value = "根据课程分类id查询课程分类")
    @GetMapping("getById/{id}")
    public R getById(@ApiParam(value = "课程分类id",required = true)@PathVariable String id){
        Subject subject = subjectService.getById(id);
        return R.ok().data("item",subject);
    }







}

