package com.pbteach.learning.web.controller;

import com.pbteach.api.learning.CourseLearningControllerApi;
import com.pbteach.framework.domain.learning.response.GetMediaResult;
import com.pbteach.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-14 11:14
 **/
@RestController
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    LearningService learningService;

    @Override
    public GetMediaResult getmedia(@PathVariable String courseId, @PathVariable String teachplanId) {

        //调用service方法，在service方法远程调用search搜索服务
        return learningService.getmedia(courseId,teachplanId);

    }
}
