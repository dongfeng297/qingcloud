package qingcloud.service;

import qingcloud.dto.Result;

public interface PayService {
    Result pay(Long orderId);

    Result payVoucher(Long orderId);
}
