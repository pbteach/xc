package com.pbteach.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-29 12:06
 **/
@Component
public class ReceiveHandler {

    //监听email队列,当接收到队列发的消息，调用此方法
    @RabbitListener(queues={"queue_inform_email"})
    public void send_email(String msg,Message message,Channel channel){
        System.out.println(msg);
    }

    //监听sms队列
    @RabbitListener(queues={"queue_inform_sms"})
    public void send_sms(String msg,Message message,Channel channel){
        System.out.println(msg);
    }


}
