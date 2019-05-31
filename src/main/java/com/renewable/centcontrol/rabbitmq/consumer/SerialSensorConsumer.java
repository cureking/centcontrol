package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.SerialSensor;
import com.renewable.centcontrol.pojo.Terminal;
import com.renewable.centcontrol.service.ISerialSensorService;
import com.renewable.centcontrol.service.ITerminalService;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
public class SerialSensorConsumer {

    @Autowired
    private ISerialSensorService iSerialSensorService;

    private static final String SERIAL_SENSOR_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-serial-sensor-terminal2centcontrol";
    private static final String SERIAL_SENSOR_TERMINAL2CENTCONTROL_QUEUE = "queue-serial-sensor-terminal2centcontrol";
    private static final String SERIAL_SENSOR_TERMINAL2CENTCONTROL_ROUTINETYPE = "topic";
    private static final String SERIAL_SENSOR_TERMINAL2CENTCONTROL_BINDINGKEY = "serial.sensor.terminal2centcontrol";

    //TODO_FINISHED 2019.05.16 完成终端机TerminalConfig的接收与判断（ID是否为长随机数，是否需要重新分配）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = SERIAL_SENSOR_TERMINAL2CENTCONTROL_QUEUE, declare = "true"),
            exchange = @Exchange(value = SERIAL_SENSOR_TERMINAL2CENTCONTROL_EXCHANGE, declare = "true", type = SERIAL_SENSOR_TERMINAL2CENTCONTROL_ROUTINETYPE),
            key = SERIAL_SENSOR_TERMINAL2CENTCONTROL_BINDINGKEY
    ))
    @RabbitHandler
    public void messageOnSerialSensor(@Payload String serialSensorListStr, @Headers Map<String, Object> headers, Channel channel) throws IOException {

        List<SerialSensor> serialSensorList = JsonUtil.string2Obj(serialSensorListStr, List.class, SerialSensor.class);

        // 2.业务逻辑
        ServerResponse response = iSerialSensorService.dealSerialSensorListFromMQ(serialSensorList);

        // 3.确认
        if (response.isSuccess()) {
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }
}
