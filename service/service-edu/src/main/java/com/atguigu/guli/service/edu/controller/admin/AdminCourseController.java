package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/admin/edu/course")
@Api(tags = "课程管理")
@CrossOrigin
public class AdminCourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    CourseDescriptionService courseDescriptionService;

    // 1、保存课程基本信息
    @PostMapping("save")
    @ApiOperation(value = "添加课程基本信息（step1）")
    public R saveCourseInfo(@RequestBody AdminCourseInfoVo adminCourseInfoBo){
        String id = courseService.saveCourseInfo(adminCourseInfoBo);
        return R.ok().data("id",id);
    }

    // 2、查询课程基本信息回显
    @ApiOperation(value = "查询课程基本信息回显")
    @GetMapping("getCourseInfo/{courseId}")
    public  R getCourseInfo(@ApiParam(value = "课程id",required = true) @PathVariable String courseId){
        AdminCourseInfoVo adminCourseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("item",adminCourseInfoVo);
    }

    // 3、查询课程列表
    @ApiOperation(value = "进行课程列表的分页条件查询")
    @GetMapping("queryPage/{pageNum}/{pageSize}")
    public R queryPage(@ApiParam(value = "页码",required = false)@PathVariable Integer pageNum,
                       @ApiParam(value = "每页显示的记录数",required = false)@PathVariable Integer pageSize,
                       @ApiParam(value = "查询条件",required = false)@RequestParam Map<String,Object> query){
        Page<AdminCourseItemVo> page = courseService.queryCourseItemVoPage(pageNum,pageSize,query);
        return R.ok().data("page",page);
    }


    // 4、更新课程基本信息
    @PutMapping("update")
    @ApiOperation(value = "更新课程基本信息（step1）")
    public R updateCourseInfo(@RequestBody AdminCourseInfoVo adminCourseInfoBo){
        String id = adminCourseInfoBo.getId();
        if (StringUtils.isEmpty(id)){
            return R.error().message("课程信息修改失败，没有课程id");
        }
        courseService.updateCourseInfo(adminCourseInfoBo);
        return R.ok().data("id",id);
    }

}

