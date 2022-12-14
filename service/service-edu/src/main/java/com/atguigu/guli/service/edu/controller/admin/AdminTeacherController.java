package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.vo.TeacherVo;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.feign.OssClient;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class AdminTeacherController {

    @Autowired
    private TeacherService teacherService;

    @Resource
    private OssClient ossclient;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("讲师列表获取成功");
    }

    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "讲师id") @PathVariable("id") String id) {
        // 删除讲师后删除oss中的头像文件
        Teacher teacher = teacherService.getById(id);
        boolean b = teacherService.deleteById(id);
        // 讲师数据删除成功：只要讲师数据库数据删除成功，头像删除成功失败都可以
        if (b && !StringUtils.isEmpty(teacher.getAvatar())) {
            // 删除讲师头像：尽量保证头像能够成功删除，如果有异常在降级兜底方法中可以保存删除失败的头像地址稍后再处理
            ossclient.delete(teacher.getAvatar(), "avatar");
        }
        return R.ok();
    }

    @ApiOperation("分页讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam(value = "讲师列表查询对象", required = false) TeacherVo teacherVo) {
        IPage<Teacher> teacherPage = teacherService.selectPage(page, limit, teacherVo);
        return R.ok().data("pageModel", teacherPage);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam(value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        boolean save = teacherService.save(teacher);
        if (save) {
            return R.ok().message("讲师对象添加成功");
        }
        return R.error().message("讲师对象添加失败");
    }

    @ApiOperation("根据id修改讲师信息")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        boolean b = teacherService.updateById(teacher);
        if (b) {
            return R.ok().message("讲师信息修改成功");
        }
        return R.error().message("讲师信息修改失败");
    }

    @ApiOperation("根据讲师id查询讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "讲师id", required = true) @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        if (!StringUtils.isEmpty(teacher)) {
            return R.ok().data("item", teacher);
        }
        return R.error().message("数据不存在");
    }


    @ApiOperation(("根据id列表删除讲师"))
    @DeleteMapping("batch-remove")
    public R removeRows(@ApiParam(value = "讲师id列表", required = true)
                        @RequestBody List<String> idList) {
        boolean result = teacherService.removeByIds(idList);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }

    }


    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(value = "查询关键字", required = true)
            @PathVariable String key) {
        List<Map<String, Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList", nameList);
    }


    @ApiOperation(value = "sentinel测试")
    @GetMapping("message1")
    public String message1() {
        return "message1";
    }


    @ApiOperation(value = "saveTest保存测试")
    @PostMapping("saveTest")
    public R saveTest(@RequestBody Teacher teacher) {
        log.info("教师信息：{}", teacher);
//        return R.ok().message("讲师添加成功！");
        return R.error().message("添加失败");
    }

    @ApiOperation("远程调用接口传入参数类型为JavaBean方式")
    @GetMapping("entity")
    public String entity() {
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId("1001");
        baseEntity.setGmtCreate(new Date());
        baseEntity.setGmtCreate(new Date());
        return ossclient.entity(baseEntity);
    }

}

