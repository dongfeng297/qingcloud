package qingcloud.service;

import org.apache.ibatis.annotations.Select;
import qingcloud.dto.Result;
import qingcloud.entity.Voucher;

import java.util.List;

public interface VoucherService  {
    Result addVoucher(Voucher voucher);

    Result getVoucher(Long id);



}
