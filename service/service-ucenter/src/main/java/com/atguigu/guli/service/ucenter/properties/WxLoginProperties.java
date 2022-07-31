package com.atguigu.guli.service.ucenter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.login")
public class WxLoginProperties {
    private String qrconnectUrl;
    private String appid;
    private String redirectUri;
    private String accessTokenUrl;
    private String appsecret;
    private String userInfoUrl;
}
