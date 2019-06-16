package com.renewable.centcontrol.rabbitmq.producer;

import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Author: jarry
 */
@Component("InitializationInclinationProducer")
public class InitializationInclinationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String INITIALIZATION_INCLINATION_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-initialization-inclination-centcontrol2terminal";
    private static final String INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_QUEUE = "queue-initialization-inclination-centcontrol2terminal";
    private static final String INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_ROUTINETYPE = "topic";
    private static final String INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_BINDINGKEY = "initialization.inclination.centcontrol2terminal";
    private static final String INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_ROUTINGKEY = "initialization.inclination.centcontrol2terminal";

    // 初始化后台相关配置（如队列等）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_QUEUE, declare = "true"),
            exchange = @Exchange(value = INITIALIZATION_INCLINATION_CENTCONTROL2TERMINAL_EXCHANGE, declare = "true", type = INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_ROUTINETYPE),
            key = INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_BINDINGKEY
    ))
    public void sendInitializationInclination(String initializationInclinationStr) throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message(initializationInclinationStr.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(INITIALIZATION_INCLINATION_CENTCONTROL2TERMINAL_EXCHANGE,
                INITIALIZATION_INCLINATION__CENTCONTROL2TERMINAL_ROUTINGKEY,
                message);
    }

    public void sendInitializationInclination(InitializationInclination initializationInclination) throws Exception {
        this.sendInitializationInclination(JsonUtil.obj2String(initializationInclination));
    }
}
