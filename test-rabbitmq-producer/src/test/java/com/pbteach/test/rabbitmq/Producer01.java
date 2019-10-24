package com.pbteach.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 测试rabbitmq 入门程序
 *
 * @author Administrator
 * @version 1.0
 * @create 2018-06-29 9:22
 **/
public class Producer01 {

    private static final String QUEUE = "helloworld";

    public static void main(String[] args) {
        //连接
        Connection connection = null;
        //通道
        Channel channel = null;
        try {
            //给MQ发送消息
            //连接MQ
            //通过连接工厂创建连接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");//IP地址
            connectionFactory.setPort(5672);//默认mq服务端口
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");//设置mq虚拟机
            //和MQ创建连接
            connection = connectionFactory.newConnection();
            //建立channel通道，会话通道，在通道中向mq发送消息
            channel = connection.createChannel();

            //声明一个队列,根据队列名称判断，如果在mq中没有此队列就创建一个队列，如果有此队列则不作处理
            /**
             * 参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             * 1、队列名称
             * 2、是否持久化，true表示持久化，当mq重启之后此队列还在
             * 3、是否独占通道，true表示此通道只能由此队列来使用
             * 4、自动删除，true表示自动删除，mq重启后队列删除
             * 5、队列参数列表
             */
            channel.queueDeclare(QUEUE,true,false,false,null);

            //发送消息
            /**
             * 参数String exchange, String routingKey, BasicProperties props, byte[] body；
             * 1、exchange 交换机名称,不指定交换机设置成空字符串，mq会使用一个默认的交换机
             * 2、routingKey 路由key，交换机跟路由key来转发消息，由于没有指定交换机，所以routingkey设置为队列的名称
             * 3、消息属性
             * 4、消息内容
             */
            String message ="hello 小明"+ System.currentTimeMillis();
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("send message.."+message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
