package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "支付订单")
public class PayOrder implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "业务订单号")
    private Long orderId;

    @ApiModelProperty(value = "支付用户id")
    private Long userId;

    @ApiModelProperty(value = "支付金额，单位分")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式 1：余额支付；2：支付宝；3：微信")
    private Integer payType;

    @ApiModelProperty(value = "支付状态 1：未支付；2：已支付；3：已取消；4：已退款")
    private Integer status;

    @ApiModelProperty(value = "支付成功时间")
    private LocalDateTime paySuccessTime;

    @ApiModelProperty(value = "退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty(value = "支付超时时间")
    private LocalDateTime payOverTime;

    @ApiModelProperty(value = "支付二维码链接")
    private String qrCodeUrl;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
