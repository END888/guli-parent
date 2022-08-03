package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "用户端短信管理")
public class SmsController {

    @Autowired
    SmsService smsService;

    // 发送短信验证码
    @GetMapping("sendMsg/{mobile}")
    public R sendMsg(@ApiParam(value = "手机号") @PathVariable String mobile){
        smsService.sendMsg(mobile);
        return R.ok();
    }
}
