package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "Course实体", description = "课程信息")
public class Course implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "课程标题", required = true)
    private String title;

    @ApiModelProperty(value = "教师ID", required = true)
    private Long teacherId;

    @ApiModelProperty(value = "封面图片URL")
    private String coverUrl;

    @ApiModelProperty(value = "课程描述")
    private String description;

    @ApiModelProperty(value = "课程价格")
    private BigDecimal price;

    @ApiModelProperty(value = "科目")
    private String subject;

    @ApiModelProperty(value = "适用年级")
    private String grade;

    @ApiModelProperty(value = "课程时长(分钟)")
    private Integer duration;

    @ApiModelProperty(value = "状态(0:未发布 1:已发布 2:下架)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "课程类型")
    private Integer courseType;
}
