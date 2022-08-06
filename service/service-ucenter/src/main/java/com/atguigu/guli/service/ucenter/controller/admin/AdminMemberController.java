package com.atguigu.guli.service.ucenter.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@RestController
@RequestMapping("/admin/ucenter/member")
public class AdminMemberController {

    @Autowired
    MemberService memberService;

    //查询指定日期注册的用户数量
    @GetMapping("getRegisterNum/{day}") // yyyy-MM-dd
    public R getRegisterNum(@PathVariable("day")String day){
        long count = memberService.count(new QueryWrapper<Member>()
                //gmtcreate:yyyy-MM-dd HH:mm:ss
                .eq("date(gmt_create)", day));
        return R.ok().data("num",count);
    }
}

