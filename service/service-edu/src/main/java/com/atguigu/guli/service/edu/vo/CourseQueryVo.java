package com.atguigu.guli.service.edu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("课程查询对象")
public class CourseQueryVo {
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "一级分类ID")
    private String subjectParentId;
    @ApiModelProperty(value = "二级分类ID")
    private String subjectId;
}
