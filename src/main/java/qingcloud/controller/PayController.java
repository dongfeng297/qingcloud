package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.service.PayService;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping
    @ApiOperation(value = "支付课程订单")
    @Log(value = "支付课程订单")
    public Result payOrder(Long orderId) {
        return payService.pay(orderId);
    }
    @PostMapping("/voucherOrder")
    @ApiOperation(value = "支付优惠券订单")
    @Log(value = "支付优惠券订单")
    public Result payVoucherOrder(Long orderId) {
        return payService.payVoucher(orderId);
    }
}
