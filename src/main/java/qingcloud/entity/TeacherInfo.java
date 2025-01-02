package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "TeacherInfo实体", description = "教师信息")
public class TeacherInfo implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "关联用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "职称")
    private String title;

    @ApiModelProperty(value = "教师简介")
    private String introduction;

    @ApiModelProperty(value = "擅长科目，多个用逗号分隔")
    private String subjects;

    @ApiModelProperty(value = "教师资格证号")
    private String certificateNo;

    @ApiModelProperty(value = "状态(0:未认证 1:已认证)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;
}