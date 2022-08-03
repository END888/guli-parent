package com.atguigu.guli.service.trade.feigin;

import com.atguigu.guli.service.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter")
public interface UCenterClient {

    @GetMapping("/api/ucenter/member/getMemberDto/{id}")
    public R getMemberDto(@ApiParam(value = "会员id") @PathVariable String id);
}
