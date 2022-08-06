package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {

    @Autowired
    OrderService orderService;

    // 1、创建订单，返回订单id
    @PostMapping("auth/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        String orderId = orderService.createOrder(courseId, memberId);
        return R.ok().data("id", orderId);
    }

    // 2、查询课程是否购买
    @GetMapping("auth/isBuy/{courseId}")
    public R isBuy(@PathVariable String courseId, HttpServletRequest request) {
        // 从token中解析出用户id
        String memberId = JwtHelper.getId(request);
        // 根据课程id、用户id、订单状态（0：未支付 1：已支付）查询 order
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId)
                .eq(Order::getStatus, 1));
        return R.ok().data("isBuy",order == null ? "0" : "1");
    }

    // 3、根据订单id查询订单详情
    // 验证用户身份，使用用户id和订单id一起查询
    @GetMapping("auth/getOrder/{id}")
    public R getOrder(@PathVariable String id,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id).eq(Order::getMemberId,memberId));
        return R.ok().data("item",order);

    }

    // 查询支付成功的状态
    @GetMapping("auth/getPayStatus/{id}")
    public R getPayStatus(@PathVariable String id){
        Order order = orderService.getById(id);
        return R.ok().data("status",order.getStatus() + "").data("courseId",order.getCourseId());
    }
}
