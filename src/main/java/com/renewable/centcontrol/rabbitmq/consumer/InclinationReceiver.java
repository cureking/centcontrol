package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.InclinationInit;
import com.renewable.centcontrol.pojo.InclinationTotal;
import com.renewable.centcontrol.service.IInclinationService;
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
public class InclinationReceiver {

    @Autowired
    private IInclinationService iInclinationService;

    //IP USERNAME PASSWORD等都是自动注入的

    //目前业务规模还很小，没必要设置太复杂的命名规则与路由规则。不过，可以先保留topic的路由策略，便于日后扩展。
    //inclinationTotal 相关配置
    private static final String INCLINATION_TOTAL_EXCHANGE = "inclination-total-data-exchange";
    private static final String INCLINATION_TOTAL_QUEUE = "inclination-total-data-queue";
    private static final String INCLINATION_TOTAL_ROUTINETYPE = "topic";
    private static final String INCLINATION_TOTAL_BINDINGKEY = "sensor.inclination.data.total";
    private static final String INCLINATION_TOTAL_ROUTINGKEY = "sensor.inclination.data.total";
    //inclinationTotal 相关配置
    private static final String INCLINATION_INIT_EXCHANGE = "inclination-init-data-exchange";
    private static final String INCLINATION_INIT_QUEUE = "inclination-init-data-queue";
    private static final String INCLINATION_INIT_ROUTINETYPE = "topic";
    private static final String INCLINATION_INIT_BINDINGKEY = "sensor.inclination.data.init";
    private static final String INCLINATION_INIT_ROUTINGKEY = "sensor.inclination.data.init";


    @RabbitListener(bindings = @QueueBinding(     // 要设置到底监听哪个QUEUE    还可以进行EXCHANGE,QUEUE,BINGDING
            value = @Queue(value = INCLINATION_TOTAL_QUEUE, declare = "true"),
            exchange = @Exchange(value = INCLINATION_TOTAL_EXCHANGE, durable = "true", type = INCLINATION_TOTAL_ROUTINETYPE),
            key = INCLINATION_TOTAL_BINDINGKEY
    )
    )
    @RabbitHandler
//    public void onInclinationMessage(@Payload Inclination inclination,
    public void onInclinationTotalMessage(@Payload String inclinationTotalListStr,
                                          @Headers Map<String, Object> headers,
                                          Channel channel) throws Exception {
        //消费者操作
        List<InclinationTotal> inclinationTotalList = JsonUtil.string2Obj(inclinationTotalListStr, List.class, InclinationTotal.class);
        ServerResponse response = iInclinationService.insertTotalDataByList(inclinationTotalList);

        if (response.isSuccess()) {
            //由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = INCLINATION_INIT_QUEUE, declare = "true"),
            exchange = @Exchange(value = INCLINATION_INIT_EXCHANGE, durable = "true", type = INCLINATION_INIT_ROUTINETYPE),
            key = INCLINATION_INIT_BINDINGKEY
    )
    )
    @RabbitHandler
//    public void onInclinationMessage(@Payload Inclination inclination,
    public void onInclinationInitMessage(@Payload String inclinationInitListStr,
                                         @Headers Map<String, Object> headers,
                                         Channel channel) throws Exception {
        //消费者操作
        List<InclinationInit> inclinationInitList = JsonUtil.string2Obj(inclinationInitListStr, List.class, InclinationTotal.class);
        ServerResponse response = iInclinationService.insertInitDataByList(inclinationInitList);

        if (response.isSuccess()) {
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }
}
