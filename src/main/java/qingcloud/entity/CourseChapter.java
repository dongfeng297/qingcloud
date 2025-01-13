package qingcloud.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(description = "课程章节表")
public class CourseChapter {

    @ApiModelProperty(value = "章节ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "课程ID", example = "10")
    private Long courseId;

    @ApiModelProperty(value = "章节标题", example = "第一章")
    private String title;

    @ApiModelProperty(value = "章节序号", example = "1")
    private Integer sort;

    @ApiModelProperty(value = "视频唯一id")
    private String videoId;

    @ApiModelProperty(value = "视频时长(秒)", example = "3600")
    private Integer duration;

    @ApiModelProperty(value = "状态(0:删除 1:可用)", example = "1")
    private Integer status;

}

