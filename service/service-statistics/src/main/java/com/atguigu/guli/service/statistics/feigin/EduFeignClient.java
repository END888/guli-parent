package com.atguigu.guli.service.statistics.feigin;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-edu")
public interface EduFeignClient {

    @GetMapping("/admin/edu/course/getCoursePublishNum/{day}")
    public R getCoursePublishNum(@PathVariable("day")String day);

    @GetMapping("/admin/edu/course/list")
    public R listAll();
}
