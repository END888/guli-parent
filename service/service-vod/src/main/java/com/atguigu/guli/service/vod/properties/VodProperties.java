package com.atguigu.guli.service.vod.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "aliyun.vod")
@Component
public class VodProperties {
    private String accessKeyId;//: LTAI5tFKSrqJT9NpwFrNjAeM
    private String accessKeySecret;//: HULmexek99zeNE2RbEsRjEdEVqtWeK
    private String templateGroupId;// 转码模板组id
    private String workflowId;// 工作流id
}
