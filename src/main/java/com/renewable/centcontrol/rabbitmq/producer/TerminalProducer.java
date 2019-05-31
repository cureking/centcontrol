package com.renewable.centcontrol.rabbitmq.producer;

import com.renewable.centcontrol.pojo.Terminal;
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
@Component("TerminalProducer")
public class TerminalProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-terminal-config-centcontrol2terminal";
    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE = "queue-terminal-config-centcontrol2terminal";
    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINETYPE = "topic";
    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_BINDINGKEY = "terminal.config.centcontrol2terminal";
    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY = "terminal.config.centcontrol2terminal";

    // 初始化后台相关配置（如队列等）
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE, declare = "true"),
            exchange = @Exchange(value = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE, declare = "true", type = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINETYPE),
            key = TERMINAL_CONFIG_CENTCONTROL2TERMINAL_BINDINGKEY
    ))


    public void sendTerminalConfig(String terminalStr) throws Exception {
//        CorrelationData correlationData = new CorrelationData();
//        correlationData.setId("11212121212");  //消息的唯一标识ID

        // 引入MessageProperties来设置contentType，以解决乱码问题
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        Message message = new Message(terminalStr.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE,
                TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY,
                message);
    }

    // 由于注解的关系，如果在上面的方法中放入Terminal参数格式，那么在其中的converAndSend中，会通过Payload注解，将message转为Terminal格式。由于转换失败，从而抛出异常。另外，这样增加一个重载方法，也是不错的选择
    public void sendTerminalConfig(Terminal terminal) throws Exception {
        this.sendTerminalConfig(JsonUtil.obj2String(terminal));
    }
}
