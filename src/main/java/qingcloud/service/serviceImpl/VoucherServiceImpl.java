package qingcloud.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qingcloud.dto.Result;
import qingcloud.entity.Voucher;
import qingcloud.mapper.VoucherMapper;
import qingcloud.service.VoucherService;

import java.time.LocalDateTime;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Override
    public Result addVoucher(Voucher voucher) {
        voucherMapper.addVoucher(voucher);
        return Result.ok();
    }


}
