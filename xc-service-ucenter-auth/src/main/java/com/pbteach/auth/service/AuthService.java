package com.pbteach.auth.service;

import com.alibaba.fastjson.JSON;
import com.pbteach.framework.client.XcServiceList;
import com.pbteach.framework.domain.ucenter.ext.AuthToken;
import com.pbteach.framework.domain.ucenter.ext.UserTokenStore;
import com.pbteach.framework.domain.ucenter.response.AuthCode;
import com.pbteach.framework.exception.ExceptionCast;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-15 11:28
 **/
@Service
public class AuthService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    /**
     * 用户认证方法
     * 1、请求spring security的/oauth/token 申请令牌
     * 2、存储到redis
     * 3、返回令牌给controller
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    public AuthToken login(String username,String password,String clientId,String clientSecret){

        //申请令牌，使用restTemplatey请求spring security
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            ExceptionCast.cast(AuthCode.AUTH_APPLYTOKEN_FAIL);
        }

        //存储令牌到redis
        boolean result = saveTokenToRedis(authToken);
        if(!result){
            ExceptionCast.cast(AuthCode.AUTH_SAVETOKEN_FAIL);
        }
        return authToken;
    }

    //存储token到redis
    public void deleteTokenFromRedis(String token){

        String key = "token:"+token;
        stringRedisTemplate.delete(key);
    }
    //存储token到redis
    private boolean saveTokenToRedis(AuthToken authToken){
        String access_token = authToken.getAccess_token();
        String key = "token:"+access_token;

        String authTokenString = JSON.toJSONString(authToken);

        //存储令牌到 redis
        stringRedisTemplate.boundValueOps(key).set(authTokenString,tokenValiditySeconds, TimeUnit.SECONDS);

        //是否存储成功, 查询token的过期时间，如果查询不到返回-1
        Long expire = stringRedisTemplate.getExpire(key);
        if(expire>0){
            return true;//存储成功
        }
        return false;
    }
    //申请令牌
    private AuthToken applyToken(String username,String password,String clientId,String clientSecret){

        //采用客户端负载均衡，从eureka获取认证服务的ip 和端口和/auth根路径
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
        String httpbasic = httpbasic(clientId, clientSecret);
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        //2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body, headers);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        //远程调用申请令牌
        ResponseEntity<Map> exchange = null;
        try {
            exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }

        // 返回的令牌信息
        Map body1 = exchange.getBody();
        String access_token = (String) body1.get("jti");
        String jwt_token = (String) body1.get("access_token");
        String refresh_token = (String) body1.get("refresh_token");
        if(StringUtils.isEmpty(access_token) ||
                StringUtils.isEmpty(jwt_token) ||
                StringUtils.isEmpty(refresh_token) ){

            //当用户不存在要响应“用户不存在”
            //"error_description" -> "UserDetailsService returned null, which is an interface contract violation"
            String error_description = (String) body1.get("error_description");
            if(error_description.indexOf("UserDetailsService returned null")>=0){
                //说明用户不存在
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            }else if(error_description.indexOf("坏的凭证")>=0){
                //当密码错误也要解析密码错误的信息，响应到客户端
                //"error_description" -> "坏的凭证"
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            }else{
                ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
            }

        }
        //将map中数据封装成AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token(access_token);
        authToken.setJwt_token(jwt_token);
        authToken.setRefresh_token(refresh_token);


        return authToken;
    }

    //拼接http basic认证串
    private String httpbasic(String clientId,String clientSecret){

        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

    //从redis查询token
    public UserTokenStore getUserToken(String token) {
        String key = "token:"+token;
        //redis中的token（json串）
        String tokenString = stringRedisTemplate.opsForValue().get(key);
        //将tokenString  这个json串转成UserTokenStore类型的对象
        try {
            UserTokenStore userTokenStore = JSON.parseObject(tokenString, UserTokenStore.class);
            return userTokenStore;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
