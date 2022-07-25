package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "阿里云文件管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {

    @Autowired
    private OssService ossService;

    /**
     * 文件上传
     *
     * @param file
     */
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module")
                    String module) {

        String path = ossService.upload(file, module);
        return R.ok().data("url", path);//返回上传成功的文件路径
    }


    @ApiOperation(value = "测试")
    @GetMapping("test")
    public R test() {
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
    public R test2(@RequestParam String str) {
        System.out.println(str);
        log.info("oss test2被调用");
        return R.ok().message("成功");
    }

    @ApiOperation(value = "测试")
    @GetMapping("test3")
    public R test2(@RequestParam R r) {
        System.out.println(r.getMessage());
        System.out.println("oss test3被调用");
        return R.ok().message("OSS返回");
    }

    @DeleteMapping("delete")
    public R delete(String path,String module){
        ossService.delete(path,module);
        return R.ok();
    }

    @ApiOperation("远程被调用接口接收请求参数为JavaBean时的处理")
    @GetMapping("entity")
    public String baseEntity(BaseEntity baseEntity) {
        System.out.println("baseEntity = " + baseEntity);
        return "Hello World";
    }

}
