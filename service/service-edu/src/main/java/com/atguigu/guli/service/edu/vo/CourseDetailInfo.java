package com.atguigu.guli.service.edu.vo;

import com.atguigu.guli.service.edu.entity.Chapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "课程详情页信息")
public class CourseDetailInfo {
    @ApiModelProperty(value = "课程id")
    private String id;
    @ApiModelProperty(value = "课程封面图片")
    private String cover;
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "销售数量")
    private Long buyCount;
    @ApiModelProperty(value = "课时数")
    private Integer lessonNum;
    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;
    @ApiModelProperty(value = "讲师id")
    private String teacherId;
    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;
    @ApiModelProperty(value = "讲师级别：1：高级讲师 2：首席讲师")
    private Integer teacherLevel;
    @ApiModelProperty(value = "讲师头像")
    private String teacherAvatar;
    @ApiModelProperty(value = "课程简介")
    private String courseDescription;
    @ApiModelProperty(value = "章节列表")
    private List<Chapter> chapterList;

}
