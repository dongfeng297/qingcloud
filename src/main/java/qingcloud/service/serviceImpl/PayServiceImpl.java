package qingcloud.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qingcloud.dto.Result;
import qingcloud.entity.CourseOrder;
import qingcloud.entity.PayOrder;
import qingcloud.entity.Voucher;
import qingcloud.entity.VoucherOrder;
import qingcloud.mapper.*;
import qingcloud.service.PayService;
import qingcloud.service.UserService;

import java.time.LocalDateTime;

import static qingcloud.constant.OrderStatusConstant.*;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private CourseOrderMapper courseOrderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    @Transactional
    public Result pay(Long orderId) {
        CourseOrder courseOrder = courseOrderMapper.getById(orderId);
        if (courseOrder == null) {
            return Result.fail("订单不存在");
        }
        if (courseOrder.getStatus()==PAID) {
            return Result.fail("订单已支付");
        }
        if(courseOrder.getStatus()==CANCELED){
            return Result.fail("订单已取消");
        }
        boolean success = userService.deductBalance(courseOrder.getUserId(),courseOrder.getPayAmount());
        if(!success){
            return Result.fail("支付失败");
        }
        courseOrderMapper.updateStatusAndPayTime(orderId,PAID, LocalDateTime.now());

        //异步通知发送邮件
        try {
            rabbitTemplate.convertAndSend("pay.direct","course.pay.success",courseOrder.getId());
        } catch (AmqpException e) {
            log.error("发送邮件失败",e);
        }

        return Result.ok();

    }

    @Override
    public Result payVoucher(Long orderId) {
        VoucherOrder voucherOrder = voucherOrderMapper.getById(orderId);
        if (voucherOrder == null) {
            return Result.fail("订单不存在");
        }
        if (voucherOrder.getStatus()==PAID) {
            return Result.fail("订单已支付");
        }
        if(voucherOrder.getStatus()==CANCELED){
            return Result.fail("订单已取消");
        }
        Long voucherId = voucherOrder.getVoucherId();
        Voucher voucher = voucherMapper.getById(voucherId);

        boolean success = userService.deductBalance(voucherOrder.getUserId(),voucher.getPayValue());
        if(!success){
            return Result.fail("支付失败");
        }
        voucherOrderMapper.update(orderId,PAID, LocalDateTime.now(),voucher.getPayValue());
        //异步通知发送邮件
        try {
            rabbitTemplate.convertAndSend("pay.direct","voucher.pay.success",voucherOrder.getId());
        } catch (AmqpException e) {
            log.error("发送邮件失败",e);
        }

        return Result.ok();
    }
}
