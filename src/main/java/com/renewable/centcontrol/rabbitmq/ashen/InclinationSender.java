package com.renewable.centcontrol.rabbitmq.ashen;

import com.renewable.centcontrol.pojo.Inclination;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Author: jarry
 */
@Component
public class InclinationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String INCLINATION_EXCHANGE = "inclination-exchange";
    private static final String INCLINATION_ROUTINGKEY = "inclination-routingkey";


    public void send(Inclination inclination) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(inclination.getMessageId());  //消息的唯一标识ID

        rabbitTemplate.convertAndSend(INCLINATION_EXCHANGE,
                INCLINATION_ROUTINGKEY,
                inclination,
                correlationData);
        //此时EXCHANGE与QUEUE，及其绑定关系，尚未确定，可以通过控制台创建。
    }
}
