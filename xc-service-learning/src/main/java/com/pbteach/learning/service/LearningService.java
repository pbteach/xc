package com.pbteach.learning.service;

import com.pbteach.framework.domain.course.TeachplanMediaPub;
import com.pbteach.framework.domain.learning.XcLearningCourse;
import com.pbteach.framework.domain.learning.response.GetMediaResult;
import com.pbteach.framework.domain.learning.response.LearningCode;
import com.pbteach.framework.domain.task.XcTask;
import com.pbteach.framework.domain.task.XcTaskHis;
import com.pbteach.framework.exception.ExceptionCast;
import com.pbteach.learning.client.SearchClient;
import com.pbteach.learning.dao.XcLearningCourseRepository;
import com.pbteach.learning.dao.XcTaskHisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-14 11:22
 **/
@Service
public class LearningService {

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;

    @Autowired
    SearchClient searchClient;

    //学生在学习页面点击课程计划，请求此方法获取视频地址
    public GetMediaResult getmedia(String courseId, String teachplanId) {

        //校验该学生是否有该课程的学习权限
        //....

        //远程调用search搜索服务
        TeachplanMediaPub getmedia = searchClient.getmedia(teachplanId);
        if(getmedia == null || StringUtils.isEmpty(getmedia.getMediaUrl())){
            //无法获取视频地址
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_NOTFOUNDVIDEOURL);
        }
        //视频地址
        String mediaUrl = getmedia.getMediaUrl();
        GetMediaResult getMediaResult = new GetMediaResult();
        getMediaResult.setFileUrl(mediaUrl);
        return getMediaResult;

    }

    //查询消息状态表记录
    public XcTaskHis getXcTaskHisById(String taskId){
        return xcTaskHisRepository.findOne(taskId);
    }

    @Transactional
    public void addcourse(String userId, String courseId, String valid, String startTime, String endTime, XcTask xcTask) {
        //向选课表添加记录
        XcLearningCourse xcLearningCourse = new XcLearningCourse();
        xcLearningCourse.setUserId(userId);
        xcLearningCourse.setValid(valid);
        xcLearningCourse.setCourseId(courseId);
        //选课状态默认为有效
        xcLearningCourse.setStatus("501001");
        //开始时间
        SimpleDateFormat simpleDateFormat  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isNotEmpty(startTime)){
            try {
                Date startTimeDate = simpleDateFormat.parse(startTime);
                xcLearningCourse.setStartTime(startTimeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        //结束时间
        if(StringUtils.isNotEmpty(endTime)){
            try {
                Date endTimeDate = simpleDateFormat.parse(endTime);
                xcLearningCourse.setEndTime(endTimeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        xcLearningCourseRepository.save(xcLearningCourse);

        //向消息表添加记录
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask,xcTaskHis);
        xcTaskHisRepository.save(xcTaskHis);

    }
}
