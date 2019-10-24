package com.pbteach.manage_course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-05 9:50
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRibbon(){

        for(int i=0;i<3;i++){
            //url中要指定不是具体的ip和端口，要从Eureka中找一个服务名
//        String url="http://localhost:31001/cms/page/get/5a795ac7dd573c04508f3a56";
            String url="http://xc-service-manage-cms/cms/page/get/5a795ac7dd573c04508f3a56";
            //使用ribbon（客户端负载均衡）调用，先根据上边服务名从eureka中找一个可用的服务
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
            Map body = forEntity.getBody();
            System.out.println(body);
        }


    }
}
