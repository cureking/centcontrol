package com.renewable.centcontrol.rabbitmq.origin;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author: jarry
 */
public class RabbitConsumer {
    private static final String QUEUE_NAME = "queue_demo2";
//    private static final String IP_ADDRESS = "47.92.249.250";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5672;

  public static void main(String[] args) throws IOException, TimeoutException,InterruptedException{
    //
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUsername("guest");
      factory.setPassword("guest");
      Address[] addresses = new Address[]{new Address(IP_ADDRESS,PORT)};

      Connection connection = factory.newConnection(addresses);

      final Channel channel = connection.createChannel();
      channel.basicQos(64);     //设置client最多接收64个未被ack的消息。

      Consumer consumer = new DefaultConsumer(channel){
          @Override
          public void handleDelivery(String consumeTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException{
              System.out.println("recv message:"+new String(body));
              long deliveryTag = envelope.getDeliveryTag();
              channel.basicAck(deliveryTag,true);                   //思考，完全可以将deliveryTag作为返回值模板的一部分，返回。在完成业务处理后，再调用一个封装方法，进行消息的确认。（毕竟rabbitMQ会保留消息很久很久）
          }
      };
      channel.basicConsume(QUEUE_NAME,consumer);
      TimeUnit.SECONDS.sleep(50000);
      channel.close();
      connection.close();
  }
}
