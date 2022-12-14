package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.vo.MemberVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
public interface MemberService extends IService<Member> {

    void register(MemberVo memberVo);

    String login(String mobile, String password);

    String wxLogin(HttpSession httpSession);

    String wxCallback(String code, String state, HttpSession session);
}
