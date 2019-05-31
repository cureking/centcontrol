package com.renewable.centcontrol.rabbitmq.producer;

import com.renewable.centcontrol.pojo.SensorRegister;
import com.renewable.centcontrol.pojo.SerialSensor;
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
@Component("SerialSensorProducer")
public class SerialSensorProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String SERIAL_SENSOR_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-serial-sensor-centcontrol2terminal";
    private static final String SERIAL_SENSOR_CENTCONTROL2TERMINAL_QUEUE = "queue-serial-sensor-centcontrol2terminal";
    private static final String SERIAL_SENSOR_CENTCONTROL2TERMINAL_ROUTINETYPE = "topic";
    private static final String SERIAL_SENSOR_CENTCONTROL2TERMINAL_BINDINGKEY = "serial.sensor.centcontrol2terminal";
    private static final String SERIAL_SENSOR_CENTCONTROL2TERMINAL_ROUTINGKEY = "serial.sensor.centcontrol2terminal";

    // 初始化后台相关配置（如队列等）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = SERIAL_SENSOR_CENTCONTROL2TERMINAL_QUEUE, declare = "true"),
            exchange = @Exchange(value = SERIAL_SENSOR_CENTCONTROL2TERMINAL_EXCHANGE, declare = "true", type = SERIAL_SENSOR_CENTCONTROL2TERMINAL_ROUTINETYPE),
            key = SERIAL_SENSOR_CENTCONTROL2TERMINAL_BINDINGKEY
    ))


    public void sendSerialSensor(String serialSensorStr) throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message(serialSensorStr.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(SERIAL_SENSOR_CENTCONTROL2TERMINAL_EXCHANGE,
                SERIAL_SENSOR_CENTCONTROL2TERMINAL_ROUTINGKEY,
                message);
    }

    public void sendSerialSensor(SerialSensor serialSensor) throws Exception {
        this.sendSerialSensor(JsonUtil.obj2String(serialSensor));
    }
}
