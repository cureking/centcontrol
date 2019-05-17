package com.renewable.centcontrol.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import com.renewable.centcontrol.pojo.Terminal;
import com.renewable.centcontrol.util.JsonUtil;
import com.renewable.centcontrol.util.PropertiesUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.renewable.centcontrol.common.constant.RabbitmqConstant.*;

/**
 * @Description：
 * @Author: jarry
 */
@Component("TerminalProducer")
public class TerminalProducer {

//
//    //TODO 完成通用数据注入问题，不可以是原来的配置，原来的配置缺乏初始化方法
//
//    @Value(RABBITMQ_HOST)
//    private static final String IP_ADDRESS;
//    private static final int PORT  = Integer.parseInt(PropertiesUtil.getProperty(RABBITMQ_PORT));
//    private static final String USER_NAME = PropertiesUtil.getProperty(RABBITMQ_USER_NAME);
//    private static final String USER_PASSWORD = PropertiesUtil.getProperty(RABBITMQ_USER_PASSWORD);
//
//    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE = "exchange-terminal-config-centcontrol2terminal";
//    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE = "queue-terminal-config-centcontrol2terminal";
//    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINETYPE = "topic";
//    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_BINDINGKEY = "terminal.config.centcontrol2terminal";
//    private static final String TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY = "terminal.config.centcontrol2terminal";
//
//    public static void sendTerminalConfig(Terminal terminal) throws IOException, TimeoutException, InterruptedException {
//
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(IP_ADDRESS);
//        factory.setPort(PORT);
//        factory.setUsername(USER_NAME);
//        factory.setPassword(USER_PASSWORD);
//
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.exchangeDeclare(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE, TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINETYPE, true, false, null);
//        channel.queueDeclare(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE, true, false, false, null);
//        channel.queueBind(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_QUEUE, TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE, TERMINAL_CONFIG_CENTCONTROL2TERMINAL_BINDINGKEY);
//
//        String terminalStr = JsonUtil.obj2String(terminal);
//        channel.basicPublish(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE, TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY, MessageProperties.PERSISTENT_TEXT_PLAIN, terminalStr.getBytes());
//
//        channel.close();
//        connection.close();
//    }
//
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    private static final String INCLINATION_EXCHANGE = "inclination-exchange";
//    private static final String INCLINATION_ROUTINGKEY = "inclination-routingkey";
//
//
//    public void send(Terminal terminal) throws Exception{
//        CorrelationData correlationData = new CorrelationData();
//        correlationData.setId(terminal.getMac());  //消息的唯一标识ID
//
//        rabbitTemplate.convertAndSend(INCLINATION_EXCHANGE,
//                INCLINATION_ROUTINGKEY,
//                terminal,
//                correlationData);
//        //此时EXCHANGE与QUEUE，及其绑定关系，尚未确定，可以通过控制台创建。
//    }

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


    public void sendTerminalConfig(Terminal terminal) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(terminal.getMac());  //消息的唯一标识ID

//        String terminalStr = JsonUtil.obj2String(terminal);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/json");
//        Message message = new Message(terminalStr.getBytes(),messageProperties);
//        rabbitTemplate.send(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE,TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY,message);



        rabbitTemplate.convertAndSend(TERMINAL_CONFIG_CENTCONTROL2TERMINAL_EXCHANGE,
                TERMINAL_CONFIG_CENTCONTROL2TERMINAL_ROUTINGKEY,
                terminal,
                correlationData);
    }
}
