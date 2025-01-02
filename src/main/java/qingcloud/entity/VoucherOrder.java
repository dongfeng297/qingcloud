package qingcloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "代金券订单实体类")
public class VoucherOrder {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "下单的用户id")
    private Long userId;

    @ApiModelProperty(value = "购买的代金券id")
    private Long voucherId;

    @ApiModelProperty(value = "支付方式 1：余额支付；2：支付宝；3：微信")
    private Integer payType;

    @ApiModelProperty(value = "订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款")
    private Integer status;

    @ApiModelProperty(value = "下单时间", example = "2023-10-01 12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "支付时间", example = "2023-10-01 12:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "核销时间", example = "2023-10-01 12:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useTime;

    @ApiModelProperty(value = "退款时间", example = "2023-10-01 12:37:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundTime;

    @ApiModelProperty(value = "更新时间", example = "2023-10-01 12:38:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}