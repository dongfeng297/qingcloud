package qingcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.Result;
import qingcloud.service.PayService;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping
    public Result payOrder(Long orderId) {
        return payService.pay(orderId);
    }
    @PostMapping("/voucherOrder")
    public Result payVoucherOrder(Long orderId) {
        return payService.payVoucher(orderId);
    }
}
