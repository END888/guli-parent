package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.utils.FormUtils;
import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.common.util.utils.MD5;
import com.atguigu.guli.service.base.consts.SmsConst;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.properties.WxLoginProperties;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.vo.MemberVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    WxLoginProperties wxLoginProperties;

    @Override
    public void register(MemberVo memberVo) {
        // 1、验证信息是否合法
        String nickname = memberVo.getNickname();
        String mobile = memberVo.getMobile();
        String password = memberVo.getPassword();
        String code = memberVo.getCode();
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password) || !FormUtils.isMobile(mobile)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 2、验证验证码是否正确: 防止表单重复提交、保证手机号注册自己的
        Object redisCode = redisTemplate.opsForValue().get(SmsConst.SMS_CODE_PREFIX + mobile);
        if (redisCode == null || !code.equals(redisCode)) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
        // 3、验证该手机号是否已经注册过
        long count = this.count(new LambdaQueryWrapper<Member>().eq(Member::getMobile, mobile));
        if (count > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }
        // 4、注册
        Member member = new Member();
        // 盐值加密
        password = MD5.encrypt(SmsConst.MEMBER_PWD_SALT + MD5.encrypt(password));
        BeanUtils.copyProperties(memberVo, member);
        member.setPassword(password);
        member.setAvatar("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg?imageView2/1/w/80/h/80");
        // 保存到数据库
        this.save(member);
        // 5、删除缓存
        redisTemplate.delete(SmsConst.SMS_CODE_PREFIX + mobile);
    }

    @Override
    public String login(String mobile, String password) {
        // 1、根据手机号校验用户是否存在
        Member member = this.getOne(new LambdaQueryWrapper<Member>().eq(Member::getMobile, mobile));
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        // 2、校验密码是否正确
        String encryptPassword = MD5.encrypt(SmsConst.MEMBER_PWD_SALT + MD5.encrypt(password));
        if (!member.getPassword().equals(encryptPassword)) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 3、校验用户状态是否正常
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 4、创建 Token并返回
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        return JwtHelper.createToken(jwtInfo);
    }

    @Override
    public String wxLogin(HttpSession httpSession) {
        try {
            String url = wxLoginProperties.getQrconnectUrl() + "?" +
                    "appid=%s" +//我们的应用在wx平台注册时分配的应用id   wxed9954c01bb89b47
                    "&redirect_uri=%s" +//用户授权登录后 的回调接口地址
                    "&response_type=code" + //访问该地址时希望得到code
                    "&scope=snsapi_login" + //访问时 授权的作用范围
                    "&state=%s" + // 防止恶意攻击的token字符串(防止表单重复提交)
                    "#wechat_redirect";
            String state = UUID.randomUUID().toString().replace("-", "");
            httpSession.setAttribute("state", state);
            url = String.format(url,
                    wxLoginProperties.getAppid(),
                    URLEncoder.encode(wxLoginProperties.getRedirectUri(), "UTF-8"),
                    state);
            return "redirect:" + url;
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON, e);
        }
    }

    @Override
    public String wxCallback(String code, String state, HttpSession session) {
        try {
            // 验证 state
            Object sessionState = session.getAttribute("state");
            if (sessionState == null || !sessionState.equals(state)){
                throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            // state使用后销毁
            session.removeAttribute("state");
            // 使用code获取wx用户的wx账号信息
            // 1、根据code获取wx中accessToken字符串（wx用户授权成功的token）
            String url = wxLoginProperties.getAccessTokenUrl() + "?" +
                    "appid=" + wxLoginProperties.getAppid() +
                    "&secret=" + wxLoginProperties.getAppsecret() +
                    "&code="+ code +
                    "&grant_type=authorization_code";
            HttpClientUtils client = new HttpClientUtils(url);
            client.get();
            // 获取请求得到的响应结果
            String content = client.getContent();
            if (StringUtils.isEmpty(content) || content.contains("errcode")){
                log.error(content);
                throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            Gson gson = new Gson();
            Map map = gson.fromJson(content, Map.class);
            String accessToken = map.get("access_token").toString();
            String openid = map.get("openid").toString();
            // openid可以代表唯一的一个微信用户
            Member member = this.getOne(new LambdaQueryWrapper<Member>().eq(Member::getOpenid, openid));
            // 从用户表查询openid对应的数据，如果没有，代表他第一次使用wx登录来访问，需要将他的数据存到数据库中
            if (member == null){
                // 2、根据openid和accessToken获取wx用户的数据
                content = getContent(accessToken, openid);
                map = gson.fromJson(content,Map.class);
                //解析出用户的数据
                String nickname = map.get("nickname").toString();
                String sex = map.get("sex").toString();
                String province = map.get("province").toString();
                String city = map.get("city").toString();
                String headimgurl = map.get("headimgurl").toString();
                String country = map.get("country").toString();

                //保存数据到数据库中
                member = new Member();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                this.save(member);
            }else if ((System.currentTimeMillis()-member.getGmtModified().getTime())>=3*24*60*60*1000){
                //  如果查询到了member对象判断他的更新时间如果超过3天 再次查询wx用户的数据更新到数据库
                //2、根据openid和accessToken获取wx用户的数据
                content = getContent(accessToken, openid);
                map = gson.fromJson(content,Map.class);
                //保存数据到数据库中
                // Base64.getEncoder().encode()  wx后端支持 utf8 mb4的编码，支持4个字节一个字符的编码，数据库默认编码时utf8
                member.setNickname(map.get("nickname").toString());
                member.setAvatar(map.get("headimgurl").toString());
                member.setGmtModified(null);//自动填充 如果属性值存在  不会自动填充
                member.setGmtCreate(null);
                this.updateById(member);
            }
            //如果if判断没有进去 代表member对象查询到了，代表用户不是第一次使用wx登录来访问

            //将wx用户的数据创建为jwt token 交给3000前端项目的首页回显
            JwtInfo jwtInfo = new JwtInfo();
            jwtInfo.setAvatar(member.getAvatar());
            jwtInfo.setNickname(member.getNickname());
            jwtInfo.setId(member.getId());
            String token = JwtHelper.createToken(jwtInfo);
            //重定向 让浏览器访问3000项目的首页 并携带token参数
            // wx登录只能在本机测试
            return "redirect:http://localhost:3000?token="+token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
    }

    private String getContent(String accessToken, String openid) throws IOException, ParseException {
        HttpClientUtils client;
        String content;
        String url;
        url = wxLoginProperties.getUserInfoUrl()+"?" +
                "access_token="+ accessToken +
                "&openid="+ openid;
        client = new HttpClientUtils(url);
        client.get();
        content = client.getContent();
        if(org.apache.commons.lang3.StringUtils.isEmpty(content)||content.contains("errcode")){
            log.error(content);
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        return content;
    }
}
