package com.pbteach.test.freemarker.controller;

import com.pbteach.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-27 9:01
 **/
@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {
    @Autowired
    RestTemplate restTemplate;

    //测试课程详情
    @RequestMapping("/course")
    public String course(Map<String,Object> map){
        //取到模型数据
        String dataUrl = "http://localhost:31200/course/courseview/4028e581617f945f01617f9dabc40000";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map model = forEntity.getBody();
        System.out.println(model);
        //设置到freemarker 数据模型中
        map.put("model",model);
        return "course";
    }
    //测试banner模板
    @RequestMapping("/banner")
    public String banner(Map<String,Object> map){
        //取到模型数据
        String dataUrl = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map banner = forEntity.getBody();
        System.out.println(banner);
        //设置到freemarker 数据模型中
        map.put("banner",banner);
        return "index_banner";
    }

    //请求test1.ftl模板
    //返回值就是模板的名称
    @RequestMapping("/test1")
    public String test1(Map<String,Object> map){//形参上map就是freemarker静态化所使用的数据模型

        //向模型中放入数据
        map.put("name","黑马");
        Student stu1 = new Student();
//        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1100.2f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
//        stu2.setMoney(200.1f);
        stu2.setAge(19);
//        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
//        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus",stus);
        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
//        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);
        //向数据模型放数据
        map.put("stuMap",stuMap);
        //模板返回的就是classpath:templates/test1.ftl模板内容
        return "test1";
    }
}
