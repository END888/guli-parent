package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.atguigu.guli.service.edu.vo.ApiCourseQueryVo;
import com.atguigu.guli.service.edu.vo.CourseDetailInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/api/edu/course")
@Api(tags = "课程管理")
public class ApiCourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;

    @ApiOperation("查询所有讲师")
    @GetMapping()
    public R list(@ApiParam(value = "查询条件")ApiCourseQueryVo apiCourseQueryVo){
        List<Course> courses = courseService.getCourseByCondition(apiCourseQueryVo);
        return R.ok().data("items",courses);
    }

    @ApiOperation("根据讲师id查询课程信息")
    @GetMapping("getCourseByTeacherId/{teacherId}")
    public R getCourseByTeacherId(@ApiParam(value = "讲师id")@PathVariable String teacherId){
        List<Course> courses = courseService.list(new LambdaQueryWrapper<Course>().eq(Course::getTeacherId, teacherId));
        return R.ok().data("items",courses);
    }

    @ApiOperation("根据课程id查询课程详情页")
    @GetMapping("getCourseDetailPageById/{id}")
    public R getCourseDetailPageById(@ApiParam(value = "课程id")@PathVariable String id){
        CourseDetailInfo courseDetailInfo = courseService.getCourseDetailPageById(id);
        return R.ok().data("item",courseDetailInfo);
    }

    @ApiOperation("查询首页热门的8个课程的数据")
    @GetMapping("getHotCourses")
    public R getHotCourses(){
        List<Course> courses = courseService.list(new LambdaQueryWrapper<Course>()
                .select(Course::getId, Course::getTitle, Course::getCover, Course::getViewCount, Course::getBuyCount,
                        Course::getPrice)
                .orderByDesc(Course::getViewCount)
                .last("limit 8"));
        return R.ok().data("items",courses);
    }

    @ApiOperation("根据课程id查询要售卖的课程信息")
    @GetMapping("getCourseDto/{id}")
    public R getCourseDto(@ApiParam(value = "课程id")@PathVariable String id){
        Course course = courseService.getById(id);
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course,courseDto);
        Teacher teacher = teacherService.getById(course.getTeacherId());
        courseDto.setTeacherName(teacher.getName());
        return R.ok().data("item",courseDto);
    }


}

