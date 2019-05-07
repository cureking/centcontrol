package com.renewable.centcontrol.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.renewable.centcontrol.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author: jarry
 */
@Slf4j
public class rabbitMQPool {

    private static ConnectionFactory factory;

    private static Connection connectionSingleton;

    //之后可以通过@Value来降低耦合度
    private static final String IP_ADDRESS = PropertiesUtil.getProperty("rabbit.server.ip");
    private static final int PORT  = Integer.parseInt(PropertiesUtil.getProperty("rabbit.server.port"));
    private static final String USERNAME  = PropertiesUtil.getProperty("rabbit.server.username");
    private static final String PASSWORD  = PropertiesUtil.getProperty("rabbit.server.password");

    private static final String Default_EXCHANGE_NAME = PropertiesUtil.getProperty("rabbit.exchange.default");
    private static final String Default_ROUTING_KEY = PropertiesUtil.getProperty("rabbit.routine.default");
    private static final String Default_QUEUE_NAME = PropertiesUtil.getProperty("rabbit.queue.default");

    static{
        initFactory();
        initConnectionSingleton();
    }

    private static void initFactory(){
        factory = new ConnectionFactory();

        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
    }

    private static void initConnectionSingleton(){
        connectionSingleton = getConnection();
    }

//    //我认为这是不安全的，暂时没有很好的方法（也许可以通过setter函数来进行修改），所以暂时注释。
//    public static void initFactory(String ip,int port,String username,String password){
//        factory = new ConnectionFactory();
//
//        factory.setHost(ip);
//        factory.setPort(port);
//        factory.setUsername(username);
//        factory.setPassword(password);
//    }

    /**
     * 获取rabbitMQ的TCP虚拟连接
     * @return
     */
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            log.error("Rabbit getConnection IOException :"+e.toString());
        } catch (TimeoutException e) {
            log.error("Rabbit getConnection TimeoutException :"+e.toString());
        }
        return connection;
    }



    public static Channel getChannelSinConnection(){        //其实可以通过设置参数来实现两者合并，但是这又涉及其他规范问题，时间不够了。。。
        if (connectionSingleton == null){
            initConnectionSingleton();
        }
        return getChannel(connectionSingleton);
    }

    /**
     * 获取rabbitMQ连接下的可复用的双向数据流虚拟信道，作为会话的基础。
     * @return
     */
    public static Channel getChannel(){
        Connection connection = getConnection();
        return getChannel(connection);
    }

    public static Channel getChannel(Connection connection){
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            log.error("Rabbit getChannel IOException :"+e.toString());
        }
        return channel;
    }

}
