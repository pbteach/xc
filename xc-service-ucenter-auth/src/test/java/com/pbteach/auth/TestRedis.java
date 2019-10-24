package com.pbteach.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-15 11:15
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        String key  ="token:464c3dbc-9ba4-4b40-9217-d6b0a6a9918b";
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTUzMTY2NDI3OSwianRpIjoiNDY0YzNkYmMtOWJhNC00YjQwLTkyMTctZDZiMGE2YTk5MThiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.FexhS4XaNF1rRJYvZbI-mftyEuk6wQe4plSZI7aGtTvMvOn2ENvBl6o74cpsgzwqV_l8IGDTd6O1Zye2GKPSRMnPHpcDQJUfjY2mJR-B9JJFWTV6BYSMc5EhVZQ8j6RoekgQ_PhgtA6jgLrYPNU0k4oQbVyPKCGHFpSKbP8nayfp6ghRCxZX0HiJdjqygu5GlZyqnoUa87-iaf-rWnlpHfnwOJH1DhnrjKc3LOp4Kce9JHUhc5-_MacCyDqwPepTC8gWuzoaCKGCxE3KlXOxzGpLL9WR4T6X2bMD0KPm0PjyInqPGy6Y5gYRS9nqHNqAt0FYk6VhXprVO6hbB8I-lQ";
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jwt,60, TimeUnit.SECONDS);

        //校验
        Long expire = stringRedisTemplate.getExpire(key);

        //根据key取数据
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);


    }
}
