package com.atguigu.guli.service.vod.controller;


import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Api("视频管理")
@RestController
@RequestMapping("/admin/vod/video")
@CrossOrigin
public class VodController {

    @Autowired
    VodService vodService;

    @ApiOperation("视频上传，并返回生成的视频id")
    @PostMapping("upload")
    public R upload(MultipartFile file){
        // 视频文件名
        String filename = file.getOriginalFilename();
        String title = UUID.randomUUID().toString().replace("-","").substring(0,16);
        String videoSourceId = null;
        try {
            InputStream stream = file.getInputStream();
            videoSourceId = vodService.upload(title, filename, stream);
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR,e);
        }
        return R.ok().data("videoSourceId",videoSourceId).message("视频上传成功");
    }


}
