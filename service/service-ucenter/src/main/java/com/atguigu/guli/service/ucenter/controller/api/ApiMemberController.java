package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ucenter/member")
@Api(tags = "用户端用户管理")
@Slf4j
public class ApiMemberController {

    @Autowired
    MemberService memberService;

    // 1、用户注册服务
    @ApiOperation("注册")
    @PostMapping("register")
    public R register(@RequestBody MemberVo memberVo){
        memberService.register(memberVo);
        return R.ok();
    }

    // 2、用户认证，生成Token
    @ApiOperation("用户认证，生成Token")
    @PostMapping("login")
    public R login(@RequestParam("mobile") String mobile,@RequestParam("password") String password){
        String token = memberService.login(mobile,password);
        return R.ok().data("token",token);
    }


    // 3、解析Token获取用户信息
    @ApiOperation("解析Token获取用户信息")
    @GetMapping("auth/getUsrInfo")
    public R getUserInfo(HttpServletRequest request){
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("item",jwtInfo);
    }



}
