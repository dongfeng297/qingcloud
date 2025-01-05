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
import qingcloud.mapper.CourseOrderMapper;
import qingcloud.mapper.UserMapper;
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

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="course.pay.success.queue",durable="true"),
            exchange = @Exchange(name = "pay.direct"),
            key="course.pay.success"
    ))
    public void courseOrderPaySuccess(Long orderId) throws MessagingException {
        try {
            log.info("收到支付成功消息，开始处理订单：{}", orderId);

            // 获取订单信息
            CourseOrder courseOrder = courseOrderMapper.getById(orderId);
            if (courseOrder == null) {
                log.error("未找到订单信息，订单号：{}", orderId);
                return;
            }
            log.debug("成功获取订单信息：{}", courseOrder);

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
            MailUtils.sendMail(email, "尊敬的用户" + name + ":\n  您在" + payTime + "支付了" + payAmount + "元,订单号为" + orderId);
            log.info("邮件发送成功，订单号：{}", orderId);

        } catch (Exception e) {
            log.error("处理支付成功消息失败，订单号：{}，异常信息：", orderId, e);
            throw e;
        }
    }
}