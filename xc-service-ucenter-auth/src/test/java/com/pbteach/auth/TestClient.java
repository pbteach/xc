package com.pbteach.auth;

import com.pbteach.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-15 11:58
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testClient(){

        //采用客户端负载均衡，从eureka获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = serviceInstance.getUri();
        String authUrl = uri+"/auth/oauth/token";
        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
        //method http的方法类型
        //requestEntity请求内容
        //responseType，将响应的结果生成的类型

        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        String httpbasic = httpbasic("XcWebApp", "XcWebApp");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        //2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");

        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body, headers);

        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);

        Map body1 = exchange.getBody();
        System.out.println(body1);

    }

    private String httpbasic(String clientId,String clientSecret){

        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}
