package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.service.VoucherOrderService;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {
    @Autowired
    private VoucherOrderService voucherOrderService;

    @PostMapping("/{id}")
    @ApiOperation(value = "下单优惠券")
    @Log(value = "下单优惠券")
    public Result orderVoucher(@PathVariable("id") Long id) {
        return voucherOrderService.orderVoucher(id);
    }


}
