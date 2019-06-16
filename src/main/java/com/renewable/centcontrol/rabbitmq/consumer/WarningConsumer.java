package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Warning;
import com.renewable.centcontrol.service.IWarningService;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class WarningConsumer {

    @Autowired
    private IWarningService iWarningService;


    // Warning 相关配置     // 这边警报的路由规则还是考虑一下吧，毕竟警报一定是订阅模型的，分发给多个消费者类型。
    private static final String WARNING_TERMINAL2CENTCONTROL_EXCHANGE = "warning-exchange-terminal2centcontrol";
    private static final String WARNING_TERMINAL2CENTCONTROL_QUEUE = "warning-inclination-queue-terminal2centcontrol";
    private static final String WARNING_TERMINAL2CENTCONTROL_ROUTINETYPE = "topic";
    private static final String WARNING_TERMINAL2CENTCONTROL_BINDINGKEY = "warning.inclination.terminal2centcontrol";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = WARNING_TERMINAL2CENTCONTROL_QUEUE, declare = "true"),
            exchange = @Exchange(value = WARNING_TERMINAL2CENTCONTROL_EXCHANGE, declare = "true", type = WARNING_TERMINAL2CENTCONTROL_ROUTINETYPE),
            key = WARNING_TERMINAL2CENTCONTROL_BINDINGKEY
    ))
    @RabbitHandler
    public void messageOnWarning(@Payload String warningListStr, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        // 1.接收数据，并校验
        List<Warning> warningList = JsonUtil.string2Obj(warningListStr, List.class, Warning.class);
        if (warningList == null || warningList.size() == 0) {
            return;
        }

        System.out.println(warningListStr);
        // 2.业务逻辑

        ServerResponse response = iWarningService.dealWarningFromMQByList(warningList);

        // 3.确认
        if (response.isSuccess()) {
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        }
    }
}
