package qingcloud.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import qingcloud.dto.Result;
import qingcloud.entity.Voucher;
import qingcloud.mapper.VoucherMapper;
import qingcloud.service.VoucherService;

import java.time.LocalDateTime;
import java.util.List;

import static qingcloud.constant.RedisConstant.SECKILL_STOCK_KEY;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result addVoucher(Voucher voucher) {
        voucherMapper.addVoucher(voucher);
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY+voucher.getId(),voucher.getStock()+"");
        return Result.ok();
    }

    @Override
    public Result getVoucher(Long id) {
         Voucher voucher=voucherMapper.getById(id);
         if(voucher==null){
             return Result.fail("优惠券不存在");
         }
         return Result.ok(voucher);
    }




}
