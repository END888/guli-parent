package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/admin/edu/video")
@CrossOrigin
@Api(tags = "课时管理")
public class AdminVideoController {

    @Autowired
    VideoService videoService;

    @ApiOperation("添加课时")
    @PostMapping("save")
    public R save(@RequestBody Video video){
        videoService.save(video);
        return R.ok().message("课时添加成功");
    }

    @ApiOperation("根据课时id删除")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "要删除的课时id") @PathVariable String id){
        videoService.removeById(id);
        return R.ok().message("课时删除成功");
    }

    @ApiOperation("修改课时信息")
    @PutMapping("update")
    public R update(@ApiParam("课时信息")@RequestBody Video video){
        videoService.updateById(video);
        return R.ok().message("课时信息修改成功");
    }

    @ApiOperation("根据课时id查询课时信息")
    @GetMapping("getById/{id}")
    public R getById(@ApiParam("课时id")@PathVariable String id){
        Video video = videoService.getById(id);
        return R.ok().data("item",video);
    }

    @ApiOperation("根据章节id查询课时列表")
    @GetMapping("getVideoListByChapterId/{chapterId}")
    public R getVideoListByChapterId(@ApiParam(value = "章节id",required = true)@PathVariable String chapterId){
        List<Video> videoList = videoService.getVideoListByChapterId(chapterId);
        return R.ok().data("items",videoList);
    }

}

