package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "课程订单表")
public class CourseOrder implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "学生用户ID")
    private Long userId;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "优惠券ID")
    private Long voucherId;

    @ApiModelProperty(value = "课程原价")
    private BigDecimal originPrice;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "支付方式(1:余额支付 2:支付宝 3:微信)")
    private Integer payType;

    @ApiModelProperty(value = "订单状态(1:未支付 2:已支付 3:已完成 4:已取消 5:退款中 6:已退款)")
    private Integer status;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
