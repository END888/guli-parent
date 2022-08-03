package com.atguigu.guli.service.sms.service.impl;

import com.atguigu.guli.common.util.utils.FormUtils;
import com.atguigu.guli.common.util.utils.RandomUtils;
import com.atguigu.guli.service.base.consts.SmsConst;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.properties.SmsProperties;
import com.atguigu.guli.service.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties({SmsProperties.class})
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    SmsProperties smsProperties;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param mobile
     */
    @Override
    public void sendMsg(String mobile) {
        // 1、校验手机号是否合法
        boolean flag = FormUtils.isMobile(mobile);
        if (!flag) {
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        // 2、判断该手机号生成验证码是否达到了单次最大频率（每分钟只能生成一个验证码）
        if (redisTemplate.hasKey(SmsConst.SMS_PRE_MIN_PREFIX + mobile)) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        // 3、判断该手机号生成验证码是否达到了每天次数限制（每天只能生成三个验证码）
        Object dayLimit = redisTemplate.opsForValue().get(SmsConst.SMS_PRE_DAY_PREFIX + mobile);
        if (dayLimit == null) {  // 代表手机号码第一次获取验证码
            redisTemplate.opsForValue().set(SmsConst.SMS_PRE_DAY_PREFIX + mobile, 0, 24, TimeUnit.HOURS);
        } else {
            int count = Integer.parseInt(dayLimit.toString());
            if (count >= 3) {
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_DAY_CONTROL);
            }
        }
        try {
            // 4、都符合条件的情况下，生成验证码
            String code = RandomUtils.getSixBitRandom();

            // 5、调用短信发送服务接口，给用户手机发送短信验证码
            // 短信平台的服务器地址：请求报文和响应报文的头不支持中文
            // 5.1、请求头参数
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "APPCODE " + smsProperties.getAppcode());
            // 5.2、请求参数
            HashMap<String, String> querys = new HashMap<>();
            querys.put("mobile", mobile);//手机号
            querys.put("param", "code:" + code);//验证码
            querys.put("tpl_id", smsProperties.getTplId());//短信模板id
            // 5.3、请求体参数
            Map<String, String> bodys = new HashMap<>();
            // 5.4、发起请求得到响应结果
//            HttpResponse response = HttpUtils.doPost(smsProperties.getHost(),
//                    smsProperties.getPath(), smsProperties.getMethod(), headers, querys, bodys);
//            String resJsonStr = EntityUtils.toString(response.getEntity());
//            Map resMap = new Gson().fromJson(resJsonStr, Map.class);
//            String returnCode = resMap.get("return_code").toString();
//            if (!returnCode.equals("00000")) {
//                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
//            }
            // 6、将验证码缓存到 redis 中（sms:mobile:手机号）
            redisTemplate.opsForValue().set(SmsConst.SMS_CODE_PREFIX + mobile,code,15,TimeUnit.MINUTES);
            // 7、给 redis 中缓存单次最大频率，并设置过期时间为2分钟
            redisTemplate.opsForValue().set(SmsConst.SMS_PRE_MIN_PREFIX + mobile,1,2,TimeUnit.MINUTES);
            // 8、给 redis 中缓存每天最大次数，并设置过期时间（每次自增+1）
            redisTemplate.opsForValue().increment(SmsConst.SMS_PRE_DAY_PREFIX + mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
