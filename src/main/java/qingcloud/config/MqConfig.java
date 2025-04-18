package qingcloud.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MqConfig {


    @Bean
    public Queue orderVoucherQueue() {
        return new Queue("orederVoucher.queue", true);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //消息处理失败重试之后仍然失败的兜底方案
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "error");
    }

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("error.exchange");
    }
    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue");
    }
    @Bean
    public Binding errorBinding() {
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with("error");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        // 配置 ReturnCallback，只需要配置一次
        rabbitTemplate.setReturnsCallback(result -> {
            log.debug("消息发送失败:exchange:{},routeKey:{},replyCode:{},replyText:{},message:{}",
                    result.getExchange(),
                    result.getRoutingKey(),
                    result.getReplyCode(),
                    result.getReplyText(),
                    result.getMessage());
        });


        return rabbitTemplate;
    }

}

