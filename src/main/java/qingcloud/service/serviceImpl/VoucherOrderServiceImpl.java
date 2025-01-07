package qingcloud.service.serviceImpl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpApplicationContextClosedException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;
import qingcloud.dto.Result;
import qingcloud.dto.UserDTO;
import qingcloud.entity.Voucher;
import qingcloud.entity.VoucherOrder;
import qingcloud.mapper.VoucherMapper;
import qingcloud.mapper.VoucherOrderMapper;
import qingcloud.service.VoucherOrderService;
import qingcloud.service.VoucherService;
import qingcloud.utils.SimpleRedisLock;
import qingcloud.utils.UserHolder;

import java.time.LocalDateTime;
import java.util.Collections;
@Slf4j
@Service
public class VoucherOrderServiceImpl implements VoucherOrderService {
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;
    @Autowired
    @Lazy
    private VoucherOrderService self;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final static DefaultRedisScript<Long> VOUCHER_SCRIPT;
    private final static String messageQueue="orederVoucher.queue";
    static {
        VOUCHER_SCRIPT = new DefaultRedisScript<>();
        VOUCHER_SCRIPT.setLocation(new ClassPathResource("voucher.lua"));
        VOUCHER_SCRIPT.setResultType(Long.class);

    }


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

        //执行lua脚本
        Long num = stringRedisTemplate.execute(
                VOUCHER_SCRIPT, Collections.emptyList(),
                id + "",  //voucherId
                UserHolder.getUser().getId() + "" //userId
        );
        int result = num.intValue();
        //返回值不为0
        if(result!=0){
            return Result.fail(result==1?"库存不足":"不要重复下单");
        }
        //加入消息队列进行下单
        long orderId= 0;

        orderId = IdUtil.getSnowflake().nextId();
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(UserHolder.getUser().getId());
        voucherOrder.setVoucherId(id);

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

        rabbitTemplate.convertAndSend(messageQueue, voucherOrder,cd);

        //返回订单id
        return Result.ok(orderId);
    }

    @RabbitListener(queues = messageQueue)
    @Override
    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        try {
            log.info("接收到消息{}", voucherOrder);
            voucherMapper.updateStock(voucherOrder.getVoucherId());
            voucherOrderMapper.addVoucherOrder(voucherOrder);
            //发送延迟消息
            try {
                rabbitTemplate.convertAndSend("delay.direct", "voucher.order.delay", voucherOrder.getId(), new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setDelay(30000);//延迟10秒方便测试
                        return message;
                    }
                });
            } catch (AmqpException e) {
                log.error("发送优惠券订单延迟消息失败", e);
            }
        } catch (Exception e) {
            log.error("创建订单失败", e);
            throw new AmqpRejectAndDontRequeueException("创建订单失败",e);
        }

    }

    /*public Result orderVoucher(Long id) {

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



//        //临时代码 TODO
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(1L);
//        UserHolder.setUser(userDTO);

        Long userId= UserHolder.getUser().getId();

        String key="order:"+userId;
        SimpleRedisLock lock = new SimpleRedisLock(stringRedisTemplate, key);
        boolean isLock = lock.tryLock(5L);
        if(!isLock){
            return Result.fail("请勿重复下单");
        }
        try {
            //加锁是为了保证共享变量（voucherOrder)的线程安全
            return self.createVoucherOrder(id);
        } finally {
            lock.unlock();
        }


    }*/

    /*@Transactional
    public Result createVoucherOrder(Long id) {
        //4.一人一单
        Long userId= UserHolder.getUser().getId();
        int count = voucherOrderMapper.query(userId,id);
        if(count>0) return Result.fail("您已经抢购过优惠券了");

        //5.扣减库存
        int row = voucherMapper.updateStock(id);
        if(row==0){
            return Result.fail("库存扣减失败");
        }

        //6.创建订单
        //雪花算法唯一生成订单id
        //long orderId = IdUtil.getSnowflake().nextId();

        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(id);
        voucherOrderMapper.addVoucherOrder(voucherOrder);


        return Result.ok(orderId);
    }*/
}
