package com.pbteach.govern.gateway.service;

import com.alibaba.fastjson.JSON;
import com.pbteach.framework.domain.ucenter.ext.UserTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 12:30
 **/
@Service
public class AuthService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

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
