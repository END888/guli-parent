package com.atguigu.guli.service.edu.controller.admin;


//import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.atguigu.guli.service.edu.vo.SubjectVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class AdminSubjectController {
    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入课程类别数据")
    @PostMapping("import")
    public R batchImport(@ApiParam(value = "Excel文件",required = true) @RequestParam("file")MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
//            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
            throw new RuntimeException();
        }
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
//        List<SubjectVo> subjectVoList = subjectService.nestedList();
        List<SubjectVo> subjectVoList = subjectService.nestedList2();
        return R.ok().data("items",subjectVoList);
    }

    @ApiOperation(value = "向指定的父分类id对应的父分类中添加子分类")
    @PostMapping("save")
    public R save( @ApiParam(value = "新增的分类信息",required = true)@RequestBody Subject subject){
        subjectService.save(subject);
        return R.ok().message("课程分类添加成功！");
    }


    @ApiOperation(value = "根据课程分类id删除课程分类")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "课程分类id",required = true) @PathVariable String id){
        // 1、首先先跟要删除的分类id去数据库中查找，看该分类还有没有子分类，如果还有子分类，则不能删除该分类
        // 其次，还要从数据库中根据该id查询一次，看能否查询到，如果查询不到，说明前端有缓存，不执行删除操作
        // 如果能查到，则继续
        Subject subject = subjectService.getById(id);
        // 2、如果没有
        if (!StringUtils.isEmpty(subject)){
            List<Subject> list = subjectService.list(new LambdaQueryWrapper<Subject>().eq(Subject::getParentId, id));
            // 没有子分类，执行删除操作
            if (StringUtils.isEmpty(list) || list.size() == 0){
                subjectService.removeById(subject);
                return R.ok().message("删除成功");
            }
            return R.error().message("该分类下还有子分类，删除失败");
        }
        return R.error().message("该分类不存在，删除失败");
    }




    @ApiOperation(value = "修改课程分类信息")
    @PutMapping("updateById")
    public R updateById(@ApiParam(value = "新增的分类信息",required = true)@RequestBody Subject subject){
        subjectService.updateById(subject);
        return R.ok().message("课程分类信息修改成功！");
    }

    @ApiOperation(value = "根据课程分类id查询课程分类")
    @GetMapping("getById/{id}")
    public R getById(@ApiParam(value = "课程分类id",required = true)@PathVariable String id){
        Subject subject = subjectService.getById(id);
        return R.ok().data("item",subject);
    }







}

