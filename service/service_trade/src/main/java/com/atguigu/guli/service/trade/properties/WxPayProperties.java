package com.atguigu.guli.service.trade.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    String appid;
    String mchid;
    String partnerkey;
    String notifyurl;
}
