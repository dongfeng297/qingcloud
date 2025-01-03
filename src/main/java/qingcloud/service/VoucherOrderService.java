package qingcloud.service;

import qingcloud.dto.Result;
import qingcloud.entity.VoucherOrder;

public interface VoucherOrderService {

    Result orderVoucher(Long id);

    void createVoucherOrder(VoucherOrder voucherOrder);
}
