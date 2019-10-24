package com.pbteach.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 11:53
 **/
@EnableDiscoveryClient//从Eureka Server获取服务
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.pbteach.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages={"com.pbteach.api"})//扫描接口
@ComponentScan(basePackages={"com.pbteach.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.pbteach.manage_cms"})//扫描本项目下的所有类
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
