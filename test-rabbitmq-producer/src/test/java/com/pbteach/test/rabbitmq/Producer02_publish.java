package com.pbteach.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class Producer02_publish {

    //email消息队列
    private static  final  String QUEUE_INFORM_EMAIL = "queue_inform_email";
    //sms消息队列
    private static  final  String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机
    private static  final  String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

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

            //声明交换机
            /**
             * String exchange, BuiltinExchangeType type
             * 1、交换机名称,采用发布订阅模式，交换机的类型要设置为fanout，Routing路由模式将交换机的类型设置为direct
             *     如果使用的是Topics模式将交换机的类型设置为topics。。使用HEADERS模式将交换机类型设置为HEADERS
             */
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            //声明一个队列,根据队列名称判断，如果在mq中没有此队列就创建一个队列，如果有此队列则不作处理
            /**
             * 参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             * 1、队列名称
             * 2、是否持久化，true表示持久化，当mq重启之后此队列还在
             * 3、是否独占通道，true表示此通道只能由此队列来使用
             * 4、自动删除，true表示自动删除，mq重启后队列删除
             * 5、队列参数列表
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            //将队列绑定到交换机中,目的就是让交换机将消息转发到队列
            /**
             * 参数：String queue, String exchange, String routingKey
             * 1、队列名称
             * 2、交换机名称
             * 3、routingKey路由key，交换机根据路由key将消息转发到队列,发布订阅模式不需要指定routingkey
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");


            //发送消息
            /**
             * 参数String exchange, String routingKey, BasicProperties props, byte[] body；
             * 1、exchange 交换机名称
             * 2、routingKey 对于发布订阅模式不需要指定routingkey
             * 3、消息属性
             * 4、消息内容
             */
            for(int i=0;i<10;i++){
                String message ="send message:"+ System.currentTimeMillis();
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,message.getBytes());
                System.out.println("send message.."+message);
            }

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
