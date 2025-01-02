package qingcloud.service.serviceImpl;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qingcloud.dto.Result;
import qingcloud.dto.UserDTO;
import qingcloud.entity.Voucher;
import qingcloud.entity.VoucherOrder;
import qingcloud.mapper.VoucherMapper;
import qingcloud.mapper.VoucherOrderMapper;
import qingcloud.service.VoucherOrderService;
import qingcloud.service.VoucherService;
import qingcloud.utils.UserHolder;

import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl implements VoucherOrderService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;
    @Autowired
    @Lazy
    private VoucherOrderService self;
    @Override

    public Result orderVoucher(Long id) {

        //1.查询优惠券
        Voucher voucher=voucherMapper.getById(id);
        if(voucher==null){
            return Result.fail("优惠券不存在");
        }
        //2.判断优惠券时间
        LocalDateTime beginTime = voucher.getBeginTime();
        if(LocalDateTime.now().isBefore(beginTime)){
            return Result.fail("优惠券抢购还未开始");
        }
        if(LocalDateTime.now().isAfter(voucher.getEndTime())){
            return Result.fail("优惠券抢购已经结束");
        }
        //3.判断库存是否充足
        int stock = voucher.getStock();
        if(stock<=0){
            return Result.fail("抱歉，你来晚了，优惠券已经被抢光了");
        }



        //临时代码 TODO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        UserHolder.setUser(userDTO);

        Long userId= UserHolder.getUser().getId();
        synchronized (userId.toString().intern()){//字符串池中返回相同的对象作为锁
            //加锁是为了保证共享变量（voucherOrder)的线程安全
            return self.createVoucherOrder(id);
            //先提交事务
        }//再释放锁

    }

    @Transactional
    public Result createVoucherOrder(Long id) {
        //4.一人一单
        Long userId= UserHolder.getUser().getId();
        int count = voucherOrderMapper.getByUserId(userId);
        if(count>0) return Result.fail("您已经抢购过优惠券了");

        //5.扣减库存
        int row = voucherMapper.updateStock(id);
        if(row==0){
            return Result.fail("库存扣减失败");
        }

        //6.创建订单
        //雪花算法唯一生成订单id
        long orderId = IdUtil.getSnowflake().nextId();

        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(id);
        voucherOrderMapper.addVoucherOrder(voucherOrder);


        return Result.ok(orderId);
    }
}
