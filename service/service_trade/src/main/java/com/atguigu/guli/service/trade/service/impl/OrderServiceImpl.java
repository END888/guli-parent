package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.utils.OrderNoUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feigin.EduClient;
import com.atguigu.guli.service.trade.feigin.UCenterClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    EduClient eduClient;
    @Autowired
    UCenterClient uCenterClient;
    @Override
    public String createOrder(String courseId, String memberId) {
        // 1、判断：如果用户已购买支付该课程，返回异常
        // 1.1、一个用户只能购买一个课程一次，使用用户id和课程id+已支付的状态，查询数据
        Order order = this.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId));
        if (order != null && order.getStatus() == 1){
            // 客户已购买，抛出异常
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }
        // 更新和新增订单，都需要查询订单需要的课程和会员的数据
        // 需要远程访问 edu 和 ucenter 服务查询
        R memberDtoR = uCenterClient.getMemberDto(memberId);
        if (memberDtoR.getCode() != 20000){
            throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
        }
        R courseDtoR = eduClient.getCourseDto(courseId);
        if (courseDtoR.getCode() != 20000){
            throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
        }
        Object memberDtoObj = memberDtoR.getData().get("item");
        Object courseDtoObj = courseDtoR.getData().get("item");

        // 由于R的data的map的泛型是String->Object,feign远程访问得到的结果是一个JSON的话，默认转为LinkedHashMap
        System.out.println(memberDtoObj.getClass().getName());
        // 将Object（HashMap）转为MemberDto对象，先将map转为JSON字符串，再将json字符串转为MemberDto对象
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto memberDto = objectMapper.convertValue(memberDtoObj, MemberDto.class);
        CourseDto courseDto = objectMapper.convertValue(courseDtoObj, CourseDto.class);
        if (order == null){
            // 2、如果用户第一次购买该课程，创建订单保存到数据库，返回订单id
            order = new Order();
        }
        // 3、判断：如果用户已下单但是未支付，更新订单信息为最新的，返回订单id
        order.setStatus(0);
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单编号
        // 会员数据
        order.setMemberId(memberId);
        order.setNickname(memberDto.getNickname());
        order.setMobile(memberDto.getMobile());
        // 订单的课程数据
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        // courseDto.getPrice()单位：元     需要转为分
        long price = courseDto.getPrice().multiply(new BigDecimal("100")).longValue();
        order.setTotalFee(price);
        order.setTeacherName(courseDto.getTeacherName());
        order.setCourseId(courseId);
        // 保存或者更新到数据库
        if (!StringUtils.isEmpty(order.getId())){
            // 订单id存在更新
            this.updateById(order);
        }else {
            // 新增
            this.save(order);
        }
        // 返回订单id
        return order.getId();
    }
}
