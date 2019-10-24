package com.pbteach.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(value={"com.pbteach.framework.domain.order","com.pbteach.framework.domain.task"})//扫描实体类
@ComponentScan(basePackages={"com.pbteach.api"})//扫描接口
@ComponentScan(basePackages={"com.pbteach.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.pbteach.order"})//扫描本项目下的所有类
@EnableScheduling //执行spring task定时任务
@SpringBootApplication
public class ManageOrderApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageOrderApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}