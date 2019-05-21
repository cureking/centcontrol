package com.renewable.centcontrol.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.renewable.centcontrol.common.ServerResponse;
import com.renewable.centcontrol.pojo.Terminal;
import com.renewable.centcontrol.service.ITerminalService;
import com.renewable.centcontrol.util.JsonUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class TerminalConsumer {

    @Autowired
    private ITerminalService iTerminalService;


    private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_EXCHANGE = "exchange-terminal-config-terminal2centcontrol";
    private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_QUEUE = "queue-terminal-config-terminal2centcontrol";
    private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_ROUTINETYPE = "topic";
    private static final String TERMINAL_CONFIG_TERMINAL2CENTCONTROL_BINDINGKEY = "terminal.config.terminal2centcontrol";

    //TODO_FINISHED 2019.05.16 完成终端机TerminalConfig的接收与判断（ID是否为长随机数，是否需要重新分配）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_QUEUE, declare = "true"),
            exchange = @Exchange(value = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_EXCHANGE, declare = "true", type = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_ROUTINETYPE),
            key = TERMINAL_CONFIG_TERMINAL2CENTCONTROL_BINDINGKEY
    ))
    @RabbitHandler
    public void messageOnTerminal(@Payload String terminalStr, @Headers Map<String,Object> headers, Channel channel)throws IOException {

        Terminal terminal = JsonUtil.string2Obj(terminalStr,Terminal.class);

        // 2.业务逻辑
        ServerResponse response = iTerminalService.getTerminalFromRabbitmq(terminal);

        // 3.确认
        if (response.isSuccess()){
            Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag,false);
        }
    }
}
