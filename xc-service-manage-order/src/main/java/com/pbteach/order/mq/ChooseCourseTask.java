package com.pbteach.order.mq;

import com.pbteach.framework.domain.task.XcTask;
import com.pbteach.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 定义任务类
 * @author Administrator
 * @version 1.0
 * @create 2018-07-20 10:04
 **/
@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;

    @RabbitListener(queues = "xc_learning_finishaddchoosecourse")
    public void receiveFinishChoosecourseTask(String taskId){
        //删除当前消息表的记录
        //向历史消息表插入记录
        taskService.finishTask(taskId);
    }

    //每隔1分钟扫描xc_task表
    @Scheduled(cron="0/10 * * * * *")
    public void sendChooseCourse(){

        //查询1分钟之前还没有执行的任务
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        //设置1分钟之前
        calendar.add(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 100);
        for(XcTask xcTask:taskList){
            //采用乐观锁方式校验此任务是否可以执行
            if(taskService.getTask(xcTask.getId(),xcTask.getVersion())>0){
                //交换机
                String mqExchange = xcTask.getMqExchange();
                //routingKey
                String mqRoutingkey = xcTask.getMqRoutingkey();
                //向mq发送任务
                taskService.publish(xcTask,mqExchange,mqRoutingkey);

                LOGGER.info("send add choose course task,taskId:{},mqExchange:{},mqRoutingkey:{}",xcTask.getId(),mqExchange,mqRoutingkey);

           }

        }


    }


    //定时执行任务
//    @Scheduled(fixedRate=3000)//每隔3秒执行
//    @Scheduled(fixedDelay=3000)//每隔3秒执行，等到上次任务完成后等3秒去执行
//    @Scheduled(cron="0/3 * * * * *")//每隔3秒执行
    public void task1(){

        LOGGER.info("=============执行任务1=============");

        try {
            //模拟执行时长为3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

//    @Scheduled(fixedDelay=3000)//每隔3秒执行，等到上次任务完成后等3秒去执行
    public void task2(){

        LOGGER.info("=============执行任务2=============");

        try {
            //模拟执行时长为3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
