package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/api/trade/wx")
public class ApiWxPayController {
    @Autowired
    OrderService orderService;

    // 1、获取code_url
    @GetMapping("auth/getCodeUrl/{orderId}")
    public R getCodeUrl(@PathVariable String orderId, HttpServletRequest request){
        return orderService.getCodeUrl(orderId,request);
    }

    // 2、支付成功的回调接口
    @PostMapping("callback")
    public String callback(HttpServletRequest request){
        return orderService.callback(request);
    }
}
