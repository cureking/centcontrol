package com.renewable.centcontrol.rabbitmq.producer;

import com.renewable.centcontrol.pojo.SensorRegister;
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
@Component("SensorRegisterProducer")
public class SensorRegisterProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String SENSOR_REGISTER_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-sensor-register-centcontrol2terminal";
    private static final String SENSOR_REGISTER_CENTCONTROL2TERMINAL_QUEUE = "queue-sensor-register-centcontrol2terminal";
    private static final String SENSOR_REGISTER_CENTCONTROL2TERMINAL_ROUTINETYPE = "topic";
    private static final String SENSOR_REGISTER_CENTCONTROL2TERMINAL_BINDINGKEY = "sensor.register.centcontrol2terminal";
    private static final String SENSOR_REGISTER_CENTCONTROL2TERMINAL_ROUTINGKEY = "sensor.register.centcontrol2terminal";

    // 初始化后台相关配置（如队列等）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = SENSOR_REGISTER_CENTCONTROL2TERMINAL_QUEUE, declare = "true"),
            exchange = @Exchange(value = SENSOR_REGISTER_CENTCONTROL2TERMINAL_EXCHANGE, declare = "true", type = SENSOR_REGISTER_CENTCONTROL2TERMINAL_ROUTINETYPE),
            key = SENSOR_REGISTER_CENTCONTROL2TERMINAL_BINDINGKEY
    ))


    public void sendSensorRegister(String sensorRegisterStr) throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message(sensorRegisterStr.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(SENSOR_REGISTER_CENTCONTROL2TERMINAL_EXCHANGE,
                SENSOR_REGISTER_CENTCONTROL2TERMINAL_ROUTINGKEY,
                message);
    }

    public void sendSensorRegister(SensorRegister sensorRegister) throws Exception {
        this.sendSensorRegister(JsonUtil.obj2String(sensorRegister));
    }
}
