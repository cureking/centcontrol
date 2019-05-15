package com.renewable.centcontrol.rabbitmq.ashen;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.pojo.Inclination;
import com.renewable.centcontrol.pojo.InclinationTotal;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class InclinationReceiver {

    //IP USERNAME PASSWORD等都是自动注入的

    //目前业务规模还很小，没必要设置太复杂的命名规则与路由规则。不过，可以先保留topic的路由策略，便于日后扩展。
    //inclinationTotal 相关配置
    private static final String INCLINATION_TOTAL_EXCHANGE = "inclination-total-data-exchange";
    private static final String INCLINATION_TOTAL_QUEUE = "inclination-total-data-queue";
    private static final String INCLINATION_TOTAL_ROUTINETYPE = "topic";
    private static final String INCLINATION_TOTAL_BINDINGKEY = "sensor.inclination.data.total";
    private static final String INCLINATION_TOTAL_ROUTINGKEY = "sensor.inclination.data.total";

    @RabbitListener(bindings = @QueueBinding(     // 要设置到底监听哪个QUEUE    还可以进行EXCHANGE,QUEUE,BINGDING
            value = @Queue(value = INCLINATION_TOTAL_QUEUE,declare = "true"),
            exchange = @Exchange(value = INCLINATION_TOTAL_EXCHANGE,durable = "true", type = INCLINATION_TOTAL_ROUTINETYPE),
            key = INCLINATION_TOTAL_BINDINGKEY
    )
    )
    @RabbitHandler
//    public void onInclinationMessage(@Payload Inclination inclination,
    public void onInclinationMessage(@Payload String inclinationTotalList,
                               @Headers Map<String,Object> headers,
                               Channel channel) throws Exception{
        //消费者操作
        System.out.println("receive message: "+inclinationTotalList);
        System.out.println("jackosn:ID: "+JsonUtil.string2Obj(inclinationTotalList, List.class,InclinationTotal.class));

//        System.out.println("receive message: ID:"+inclinationTotal.getId());
        System.out.println(Arrays.toString(headers.values().toArray()));

        //由于配置中写的是手动签收，所以这里需要通过Headers来进行签收
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);

    }
}
