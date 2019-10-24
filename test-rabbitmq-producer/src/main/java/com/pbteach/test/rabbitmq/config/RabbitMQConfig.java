package com.pbteach.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 配置类
 * @author Administrator
 * @version 1.0
 * @create 2018-06-29 11:47
 **/
@Configuration
public class RabbitMQConfig {

    //email消息队列
    public static  final  String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static  final  String ROUTINGKEY_EMAIL = "inform.#.email.#";
    //sms消息队列
    public static  final  String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static  final  String ROUTINGKEY_SMS = "inform.#.sms.#";
    //交换机
    public static  final  String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        //声明交换机
        //指定交换机名称,durable(true)表示持久化
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }


    //声明队列
    //声明email队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }
    //声明sms队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //绑定队列到交换机
    //绑定email队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                               @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        //with()指定路由key
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }

    //绑定sms队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        //with()指定路由key
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }



}
