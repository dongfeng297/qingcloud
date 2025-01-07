package qingcloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import qingcloud.entity.CourseOrder;
import qingcloud.entity.User;
import qingcloud.entity.Voucher;
import qingcloud.entity.VoucherOrder;
import qingcloud.mapper.CourseOrderMapper;
import qingcloud.mapper.UserMapper;
import qingcloud.mapper.VoucherOrderMapper;
import qingcloud.utils.MailUtils;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class PaySuccessListener {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseOrderMapper courseOrderMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="course.pay.success.queue",durable="true"),
            exchange = @Exchange(name = "pay.direct"),
            key="course.pay.success"
    ))
    public void courseOrderPaySuccess(Long orderId) throws MessagingException {
        try {
            // 获取订单信息
            CourseOrder courseOrder = courseOrderMapper.getById(orderId);
            if (courseOrder == null) {
                log.error("未找到订单信息，订单号：{}", orderId);
                return;
            }
            // 获取用户信息
            Long userId = courseOrder.getUserId();
            User user = userMapper.getById(userId);
            if (user == null) {
                log.error("未找到用户信息，用户ID：{}", userId);
                return;
            }
            log.debug("成功获取用户信息：{}", user);

            String email = user.getEmail();
            String name = user.getName();
            BigDecimal payAmount = courseOrder.getPayAmount();
            LocalDateTime payTime = courseOrder.getPayTime();

            // 发送邮件
            log.info("准备发送邮件到用户邮箱：{}", email);
            MailUtils.sendMail(email, "尊敬的用户" + name + ":\n  您在" + payTime + "为购买课程支付了" + payAmount + "元,订单号为" + orderId);
            log.info("邮件发送成功，订单号：{}", orderId);
        } catch (Exception e) {
            log.error("处理支付成功消息失败，订单号：{}，异常信息：", orderId, e);
            throw e;
        }
    }
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="voucher.pay.success.queue",durable="true"),
            exchange = @Exchange(name = "pay.direct"),
            key="voucher.pay.success"
    ))
    public void voucherOrderPaySuccess(Long orderId) throws MessagingException {
        try {
            // 获取订单信息
            VoucherOrder voucherOrder = voucherOrderMapper.getById(orderId);
            if (voucherOrder == null) {
                log.error("未找到订单信息，订单号：{}", orderId);
                return;
            }
            // 获取用户信息
            Long userId = voucherOrder.getUserId();
            User user = userMapper.getById(userId);
            if (user == null) {
                log.error("未找到用户信息，用户ID：{}", userId);
                return;
            }
            String email = user.getEmail();
            String name = user.getName();
            BigDecimal payAmount = voucherOrder.getPayAmount();
            LocalDateTime payTime = voucherOrder.getPayTime();

            // 发送邮件
            MailUtils.sendMail(email, "尊敬的用户" + name + ":\n  您在" + payTime + "为购买优惠券支付了" + payAmount + "元,订单号为" + orderId);
            log.info("邮件发送成功，订单号：{}", orderId);

        } catch (Exception e) {
            log.error("处理支付成功消息失败，订单号：{}，异常信息：", orderId, e);
            throw e;
        }
    }


}