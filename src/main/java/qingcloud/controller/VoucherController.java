package qingcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.dto.Result;
import qingcloud.entity.Voucher;
import qingcloud.service.VoucherService;

@RequestMapping("/voucher")
@RestController
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        return voucherService.addVoucher(voucher);
    }
    @GetMapping("/{id}")
    public Result getVoucher(@PathVariable("id") Long id) {
        return voucherService.getVoucher(id);
    }




}
