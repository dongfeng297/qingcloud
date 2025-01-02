package qingcloud.service;

import qingcloud.dto.Result;

public interface VoucherOrderService {

    Result orderVoucher(Long id);

    Result createVoucherOrder(Long id);
}
