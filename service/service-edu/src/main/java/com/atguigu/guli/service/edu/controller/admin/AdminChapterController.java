package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.vo.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/admin/edu/chapter")
@Api(tags = "章节管理")
public class AdminChapterController {

    @Autowired
    ChapterService chapterService;


    @ApiOperation("根据课程id查询章节列表")
    @GetMapping("getChapterListByCourseId/{courseId}")
    public R getChapterListByCourseId(@ApiParam(value = "课程id",required = true) @PathVariable String courseId){
        List<ChapterVo> chapterVoList = chapterService.getChapterListByCourseId(courseId);
        return R.ok().data("items",chapterVoList);
    }

    @ApiOperation("添加章节信息")
    @PostMapping("save")
    public R save(@ApiParam(value = "章节信息")@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return R.ok().message("添加成功");
    }

    @ApiOperation("根据章节id删除章节")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "要删除的章节id")@PathVariable String id){
        chapterService.deleteById(id);
        return R.ok().message("删除成功");
    }

    @ApiOperation("修改章节信息")
    @PutMapping("update")
    public R update(@ApiParam(value = "章节信息")@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return R.ok().message("修改成功");
    }

}

