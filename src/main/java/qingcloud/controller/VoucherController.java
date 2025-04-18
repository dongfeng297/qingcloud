package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.entity.Voucher;
import qingcloud.service.VoucherService;

@RequestMapping("/voucher")
@RestController
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping
    @ApiOperation(value = "添加优惠券")
    @Log(value = "添加优惠券")
    public Result addVoucher(@RequestBody Voucher voucher) {
        return voucherService.addVoucher(voucher);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "查询优惠券")
    public Result getVoucher(@PathVariable("id") Long id) {
        return voucherService.getVoucher(id);
    }




}
