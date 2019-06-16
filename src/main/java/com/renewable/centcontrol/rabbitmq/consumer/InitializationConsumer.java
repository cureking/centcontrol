package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InclinationInit;
import com.renewable.centcontrol.pojo.InclinationTotal;
import com.renewable.centcontrol.pojo.InitializationInclination;
import com.renewable.centcontrol.service.IInclinationService;
import com.renewable.centcontrol.service.IInitializationInclinationService;
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
public class InitializationConsumer {

    @Autowired
    private IInitializationInclinationService iInitializationInclinationService;


    //IP USERNAME PASSWORD等都是自动注入的

    //目前业务规模还很小，没必要设置太复杂的命名规则与路由规则。不过，可以先保留topic的路由策略，便于日后扩展。
    // Initialization_Inclination 相关配置
    private static final String INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_EXCHANGE = "initialization-inclination-exchange-terminal2centcontrol";
    private static final String INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_QUEUE = "initialization-inclination-queue-terminal2centcontrol";
    private static final String INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_ROUTINETYPE = "topic";
    private static final String INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_BINDINGKEY = "sensor.initialization.inclination.terminal2centcontrol";

    @RabbitListener(bindings = @QueueBinding(     // 要设置到底监听哪个QUEUE    还可以进行EXCHANGE,QUEUE,BINGDING
            value = @Queue(value = INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_QUEUE, declare = "true"),
            exchange = @Exchange(value = INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_EXCHANGE, durable = "true", type = INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_ROUTINETYPE),
            key = INITIALIZATION_INCLINATION_TERMINAL2CENTCONTROL_BINDINGKEY
    )
    )
    @RabbitHandler
    public void onInitializationInclinationMessage(@Payload String initializationInclinationListStr,
                                          @Headers Map<String, Object> headers,
                                          Channel channel) throws Exception {
        //消费者操作
        List<InitializationInclination> initializationInclinationList = JsonUtil.string2Obj(initializationInclinationListStr, List.class, InitializationInclination.class);

        ServerResponse response = iInitializationInclinationService.receiveInitializationInclinationListFromMQ(initializationInclinationList);

        if (response.isSuccess()) {
            //由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }
}
