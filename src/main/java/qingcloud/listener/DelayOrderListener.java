package qingcloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qingcloud.constant.RedisConstant;
import qingcloud.entity.CourseOrder;
import qingcloud.entity.VoucherOrder;
import qingcloud.mapper.CourseOrderMapper;
import qingcloud.mapper.VoucherMapper;
import qingcloud.mapper.VoucherOrderMapper;

import java.time.LocalDateTime;
import java.util.Map;

import static qingcloud.constant.OrderStatusConstant.CANCELED;
import static qingcloud.constant.OrderStatusConstant.UNPAID;

@Slf4j
@Component
public class DelayOrderListener {
    @Autowired
    private CourseOrderMapper courseOrderMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "course.order.delay.queue"),
            exchange = @Exchange(name = "delay.direct",delayed = "true"),
            key="course.order.delay"
    ))
    public void delayCourseOrder(Map<String,Long> map){
        Long orderId = map.get("courseOrderId");
        Long voucherOrderId = map.get("voucherOrderId");
        CourseOrder courseOrder = courseOrderMapper.getById(orderId);
        //回滚优惠券状态为已支付
        if(courseOrder.getVoucherId()!=null){
            voucherOrderMapper.rallbackStatus(voucherOrderId,UNPAID);
            log.info("回滚优惠券状态成功");
        }

        if(courseOrder.getStatus()==UNPAID){
            courseOrderMapper.updateStatusAndPayOverTime(orderId,CANCELED, LocalDateTime.now());
            log.info("取消课程订单成功");
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "voucher.order.delay.queue"),
            exchange = @Exchange(name = "delay.direct",delayed = "true"),
            key="voucher.order.delay"
    ))
    public void delayVoucherOrder(Long orderId){
        VoucherOrder voucherOrder = voucherOrderMapper.getById(orderId);
        if(voucherOrder.getStatus()==UNPAID){
            voucherOrderMapper.updateStatusAndPayOverTime(orderId,CANCELED, LocalDateTime.now());
            log.info("取消优惠券订单成功");
            //回滚库存
            voucherMapper.rollbackStock(voucherOrder.getVoucherId());
            stringRedisTemplate.opsForValue().increment(RedisConstant.SECKILL_STOCK_KEY + voucherOrder.getVoucherId());
            log.info("回滚库存成功");
        }
    }



}
