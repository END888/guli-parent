package com.atguigu.guli.service.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {
    private String host;
    private String path;
    private String method;
    private String appcode;
    private String tplId;
}
