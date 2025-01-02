package qingcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.Result;
import qingcloud.service.VoucherOrderService;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {
    @Autowired
    private VoucherOrderService voucherOrderService;

    @PostMapping("/{id}")
    public Result orderVoucher(@PathVariable("id") Long id) {
        return voucherOrderService.orderVoucher(id);
    }
}
