package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@Api(tags = "调用微信第三方接口")
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {



    @Autowired
    MemberService memberService;
    // 显示微信验证码
    @GetMapping("login")
    public String wxLogin(HttpSession httpSession){
        return memberService.wxLogin(httpSession);
    }

    // 微信回调方法
    //http://localhost:8160/api/ucenter/wx/callback
    @GetMapping("callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpSession session){
        return memberService.wxCallback(code,state,session);
    }
}
