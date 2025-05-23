package qingcloud.service.serviceImpl;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;
import qingcloud.dto.Result;
import qingcloud.entity.CourseOrder;
import qingcloud.entity.PayOrder;
import qingcloud.entity.Voucher;
import qingcloud.entity.VoucherOrder;
import qingcloud.event.CourseStaticEvent;
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
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

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

        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex){
                log.error("消息发送失败",ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if(result.isAck()){
                    log.debug("消息发送成功,收到ACK");
                }else{
                    log.error("消息发送失败,收到NACK,reason:{}",result.getReason());
                }
            }
        });
        rabbitTemplate.convertAndSend("pay.direct","course.pay.success",courseOrder.getId(),cd);

        //异步通知课程购买数+1
        applicationEventPublisher.publishEvent(CourseStaticEvent.purchaseEvent(courseOrder.getCourseId()));

        return Result.ok();

    }

    @Override
    @Transactional
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
        //异步通知发送邮件（最好用spring事件）

        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息发送失败",ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if(result.isAck()){
                    log.debug("消息发送成功,收到ACK");
                }else{
                    log.error("消息发送失败,收到NACK,reason:{}",result.getReason());
                }
            }
        });
        rabbitTemplate.convertAndSend("pay.direct","voucher.pay.success",voucherOrder.getId(),cd);
        

        return Result.ok();
    }


}
