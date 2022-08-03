package com.atguigu.guli.service.trade.feigin;


import com.atguigu.guli.service.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-edu")
public interface EduClient {

    @GetMapping("/api/edu/course/getCourseDto/{id}")
    public R getCourseDto(@ApiParam(value = "课程id")@PathVariable String id);
}
