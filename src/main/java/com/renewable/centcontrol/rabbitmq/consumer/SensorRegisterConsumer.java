package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.pojo.SensorRegister;
import com.renewable.centcontrol.service.IInitializationInclinationService;
import com.renewable.centcontrol.service.ISensorRegisterService;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class SensorRegisterConsumer {

    @Autowired
    private ISensorRegisterService iSensorRegisterService;


    private static final String SENSOR_REGISTER_TERMINAL2CENTCONTROL_EXCHANGE = "sensor-register-exchange-terminal2centcontrol";
    private static final String SENSOR_REGISTER_TERMINAL2CENTCONTROL_QUEUE = "sensor-register-queue-terminal2centcontrol";
    private static final String SENSOR_REGISTER_TERMINAL2CENTCONTROL_ROUTINETYPE = "topic";
    private static final String SENSOR_REGISTER_TERMINAL2CENTCONTROL_BINDINGKEY = "sensor.register.config.terminal2centcontrol";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = SENSOR_REGISTER_TERMINAL2CENTCONTROL_QUEUE, declare = "true"),
            exchange = @Exchange(value = SENSOR_REGISTER_TERMINAL2CENTCONTROL_EXCHANGE, durable = "true", type = SENSOR_REGISTER_TERMINAL2CENTCONTROL_ROUTINETYPE),
            key = SENSOR_REGISTER_TERMINAL2CENTCONTROL_BINDINGKEY
    )
    )
    @RabbitHandler
    public void onSensorRegisterMessage(@Payload String sensorRegisterListStr,
                                          @Headers Map<String, Object> headers,
                                          Channel channel) throws Exception {
        //消费者操作
        List<SensorRegister> sensorRegisterList = JsonUtil.string2Obj(sensorRegisterListStr, List.class, SensorRegister.class);

        ServerResponse response = iSensorRegisterService.receiveSensorRegisterListFromMQ(sensorRegisterList);

        if (response.isSuccess()) {
            //由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }
}
