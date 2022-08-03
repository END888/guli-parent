package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    OrderService orderService;

    // 创建订单，返回订单id
    @PostMapping("auth/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        String orderId = orderService.createOrder(courseId,memberId);
        return R.ok().data("id",orderId);
    }
}
