package com.renewable.centcontrol.rabbitmq.origin;



import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author: jarry
 */
public class RabbitProducer {
    //消费者client

    private static final String EXCHANGE_NAME = "exchange_demo2";
    private static final String ROUTING_KEY = "routingkey_demo2";
    private static final String QUEUE_NAME = "queue_demo2";
    //    private static final String IP_ADDRESS = "47.92.249.250";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT  = 5672;

  public static void main(String[] args) throws IOException, TimeoutException,InterruptedException {
    //
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(IP_ADDRESS);
      factory.setPort(PORT);
      factory.setUsername("guest");
      factory.setPassword("guest");

      Connection connection = factory.newConnection();

      Channel channel = connection.createChannel();
      channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);      //String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
      channel.queueDeclare(QUEUE_NAME,true,false,false,null);               //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
      channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

//      String message = "Hello World!";
//      channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());


      //test
      for (int i = 1;i<=50000;i++){
          String message = "Hello Everybody! This is NO."+i+" message.";
          channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
          Thread.currentThread().sleep(20);
      }

      channel.close();
      connection.close();
  }
}
