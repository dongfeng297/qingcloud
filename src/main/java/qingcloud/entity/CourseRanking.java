package qingcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(description = "课程排名信息")
@Data
public class CourseRanking implements Serializable {

    @ApiModelProperty("排名ID")
    private Long rankingId;

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("排名位置")
    private Integer rankingPosition;

}

