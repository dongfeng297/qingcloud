package qingcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@ApiModel(description = "优惠券")
public class Voucher {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "代金券标题", required = true)
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "使用规则")
    private String rules;

    @ApiModelProperty(value = "实际支付金额", required = true)
    private BigDecimal payValue;

    @ApiModelProperty(value = "原价金额", required = true)
    private BigDecimal actualValue;

    @ApiModelProperty(value = "库存", required = true)
    private Integer stock;

    @ApiModelProperty(value = "状态(1:上架, 2:下架, 3:过期)", example = "1", required = true)
    private Integer status;

    @ApiModelProperty(value = "抢购开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "抢购结束时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

