package com.pbteach.order.service;

import com.pbteach.framework.domain.task.XcTask;
import com.pbteach.framework.domain.task.XcTaskHis;
import com.pbteach.order.dao.XcTaskHisRepository;
import com.pbteach.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-20 11:04
 **/
@Service
public class TaskService {
    @Autowired
    XcTaskRepository xcTaskRepository;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //一次查询n条任务，查询某个时间点之前的任务
    public List<XcTask> findTaskList(Date updateTime,int n){

        //设置分页参数
        Pageable pageable = new PageRequest(0,n);//查询n条任务
        Page<XcTask> content = xcTaskRepository.findByUpdateTimeBefore(pageable, updateTime);
        List<XcTask> list = content.getContent();
        return list;
    }



    /**
     *  //发送消息
     * @param xcTask 任务对象
     * @param ex 交换机id
     * @param routingKey
     */
    @Transactional
    public void publish(XcTask xcTask,String ex,String routingKey){

       //查询任务
        XcTask one = xcTaskRepository.findOne(xcTask.getId());
        if(one!=null){
            //String exchange, String routingKey, Object object
            rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
            //更新任务时间为当前时间
            one.setUpdateTime(new Date());
            xcTaskRepository.save(one);
        }

    }

    @Transactional
    public int getTask(String taskId, Integer version){
        return xcTaskRepository.updateTaskVersion(taskId,version);
    }

    @Transactional
    public void finishTask(String taskId){
        //从当前消息表查询
        XcTask xcTask = xcTaskRepository.findOne(taskId);
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask,xcTaskHis);
        xcTaskHisRepository.save(xcTaskHis);
        xcTaskRepository.delete(taskId);

    }
}
