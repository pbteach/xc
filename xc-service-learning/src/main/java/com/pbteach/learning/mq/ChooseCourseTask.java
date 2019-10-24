package com.pbteach.learning.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.pbteach.framework.domain.task.XcTask;
import com.pbteach.framework.domain.task.XcTaskHis;
import com.pbteach.learning.config.RabbitMQConfig;
import com.pbteach.learning.service.LearningService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-20 11:54
 **/
@Component
public class ChooseCourseTask {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    LearningService learningService;

    //监听添加选课队列
    @RabbitListener(queues="xc_learning_addchoosecourse")
    public void receiveChoosecourseTask(XcTask xcTask,Message message,Channel channel){
        //消息id
        String taskId = xcTask.getId();
        //根据消息id从消息表查询，如果查询不到再添加选课
        XcTaskHis xcTaskHis = learningService.getXcTaskHisById(taskId);
        if(xcTaskHis == null){
            //获取消息之后，解析出消息内容
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            //解析出字段内容
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            String startTime = (String) map.get("startTime");
            String endTime = (String) map.get("endTime");

            //向选课表添加记录
            learningService.addcourse(userId,courseId,valid,startTime,endTime,xcTask);

            //向完成选课队列发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,taskId);

        }else{
            //向完成选课队列发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,taskId);

        }




    }
}
