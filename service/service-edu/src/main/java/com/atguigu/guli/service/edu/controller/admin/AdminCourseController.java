package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.atguigu.guli.service.edu.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.vo.CourseQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class AdminCourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CourseDescriptionService courseDescriptionService;

    // 1、保存课程基本信息
    @PostMapping("save")
    @ApiOperation(value = "添加课程基本信息（step1）")
    public R saveCourseInfo(@RequestBody AdminCourseInfoVo adminCourseInfoBo) {
        String id = courseService.saveCourseInfo(adminCourseInfoBo);
        return R.ok().data("id", id);
    }

    // 2、查询课程基本信息回显
    @ApiOperation(value = "查询课程基本信息回显")
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@ApiParam(value = "课程id", required = true) @PathVariable String id) {
        AdminCourseInfoVo adminCourseInfoVo = courseService.getCourseInfo(id);
        return R.ok().data("item", adminCourseInfoVo);
    }

    // 3、查询课程列表
    // SpringMVC默认接收参数是以请求参数接收的
    @ApiOperation(value = "进行课程列表的分页条件查询")
    @GetMapping("queryPage/{pageNum}/{pageSize}")
    public R queryPage(@ApiParam(value = "页码", required = false) @PathVariable Integer pageNum,
                       @ApiParam(value = "每页显示的记录数", required = false) @PathVariable Integer pageSize,
                       @ApiParam(value = "查询参数",required = false)CourseQueryVo courseQueryVo) {
        System.out.println(courseQueryVo);
        Page<AdminCourseItemVo> page = courseService.queryCourseItemVoPage(pageNum, pageSize,courseQueryVo);
        return R.ok().data("page",page);
    }


    // 4、更新课程基本信息
    @PutMapping("update")
    @ApiOperation(value = "更新课程基本信息（step1）")
    public R updateCourseInfo(@RequestBody AdminCourseInfoVo adminCourseInfoBo) {
        String id = adminCourseInfoBo.getId();
        if (StringUtils.isEmpty(id)) {
            return R.error().message("课程信息修改失败，没有课程id");
        }
        courseService.updateCourseInfo(adminCourseInfoBo);
        return R.ok().data("id", id);
    }

    // 5、根据课程id删除课程
    @ApiOperation(value = "根据课程id删除课程")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "课程id", required = true) @PathVariable String id) {
        courseService.deleteCourseInfo(id);
        return R.ok();
    }

    // 5、查询课程的发布信息
    @ApiOperation(value = "查询课程的发布信息")
    @GetMapping("getPublishInfoById/{id}")
    public R getPublishInfoById(@ApiParam(value = "课程id", required = true) @PathVariable String id) {
        AdminCourseItemVo item = courseService.getPublishInfoById(id);
        return R.ok().data("item", item);
    }

    // 6、发布课程
    @ApiOperation(value = "发布课程")
    @PutMapping("publishCourseById/{id}")
    public R publishCourseById(@ApiParam(value = "要发布的课程id", required = true) @PathVariable String id) {
        Boolean result = courseService.publishCourse(id);
        if (result) {
            return R.ok().message("课程发布成功");
        }
        return R.error().message("课程发布失败");
    }



}

