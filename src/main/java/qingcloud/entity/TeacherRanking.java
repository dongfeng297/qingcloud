package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "课程排名信息")
@Data
public class TeacherRanking implements Serializable {

    @ApiModelProperty("排名ID")
    private Long rankingId;

    @ApiModelProperty("老师的用户ID")
    private Long teacherId;

    @ApiModelProperty("排名位置")
    private Integer rankingPosition;

}