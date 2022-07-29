package com.atguigu.guli.service.edu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户端课程查询实体类")
public class ApiCourseQueryVo {

    @ApiModelProperty("一级分类id")
    private String subjectParentId;
    @ApiModelProperty("二级分类id")
    private String subjectId;
    @ApiModelProperty("筛选列：1【销量：默认】、2【时间】、3【价格】")
    private Integer filterColumn = 1;
    @ApiModelProperty("排序方式：0【降序：默认】、1【升序】")
    private Integer orderType = 0;
}
