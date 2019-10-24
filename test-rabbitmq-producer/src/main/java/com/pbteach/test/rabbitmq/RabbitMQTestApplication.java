package com.pbteach.test.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 11:53
 **/
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
public class RabbitMQTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQTestApplication.class,args);
    }

}
