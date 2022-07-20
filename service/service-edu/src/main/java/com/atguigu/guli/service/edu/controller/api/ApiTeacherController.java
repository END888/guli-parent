package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.bo.TeacherQuery;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/api/edu/teacher")
@Slf4j
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public R listAll() {
        log.debug("{}级别日志", "debug");
        log.info("{}级别日志", "info");
        log.warn("{}级别日志", "warn");
        log.error("{}级别日志", "error");
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("讲师列表获取成功");
    }

    @ApiOperation(value = "根据讲师ID删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID", required = true) @PathVariable String id) {
        boolean result = teacherService.removeById(id);
        if (result) {
            return R.ok().message("讲师删除成功");
        }
        return R.error().message("讲师删除失败");
    }

    @ApiOperation("分页讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam(value = "讲师列表查询对象",required = false)TeacherQuery teacherQuery) {
        IPage<Teacher> teacherPage = teacherService.selectPage(page, limit, teacherQuery);
        return R.ok().data("pageModel",teacherPage);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam(value = "讲师对象",required = true)@RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if (save){
            return R.ok().message("讲师对象添加成功");
        }
        return R.error().message("讲师对象添加失败");
    }

    @ApiOperation("根据id修改讲师信息")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "讲师对象",required = true)@RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if (b){
            return R.ok().message("讲师信息修改成功");
        }
        return R.error().message("讲师信息修改失败");
    }

    @ApiOperation("根据讲师id查询讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "讲师id",required = true)@PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if (!StringUtils.isEmpty(teacher)){
            return R.ok().data("item",teacher);
        }
        return R.error().message("数据不存在");
    }

}

