package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     * @param file
     */
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module")
                    String module) throws IOException {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);

            //返回r对象
            return R.ok().message("文件上传成功").data("url", uploadUrl);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }


    @ApiOperation(value = "测试")
    @GetMapping("test")
    public R test(){
        log.info("oss test被调用");
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return R.ok();
    }

    @ApiOperation(value = "测试")
    @GetMapping("test2")
    public R test2(@RequestParam String str){
        System.out.println(str);
        log.info("oss test2被调用");
        return R.ok().message("成功");
    }
    @ApiOperation(value = "测试")
    @GetMapping("test3")
    public R test2(@RequestParam R r){
        System.out.println(r.getMessage());
        System.out.println("oss test3被调用");
        return R.ok().message("OSS返回");
    }

    @ApiOperation(value = "文件删除")
    @DeleteMapping("remove")
    public R remove(@ApiParam(value = "要删除的文件路径",required = true)@RequestBody String url){
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }
}
