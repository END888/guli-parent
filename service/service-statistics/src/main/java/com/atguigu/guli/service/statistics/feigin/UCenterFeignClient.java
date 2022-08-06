package com.atguigu.guli.service.statistics.feigin;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter")
public interface UCenterFeignClient {

    @GetMapping("/admin/ucenter/member/getRegisterNum/{day}") // yyyy-MM-dd
    public R getRegisterNum(@PathVariable("day")String day);
}
