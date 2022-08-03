package com.atguigu.guli.service.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberDto {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("用户名")
    private String nickname;
}
