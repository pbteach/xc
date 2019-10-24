package com.pbteach.search.web.controller;

import com.pbteach.api.search.EsCourseControllerApi;
import com.pbteach.framework.domain.course.CoursePub;
import com.pbteach.framework.domain.course.TeachplanMediaPub;
import com.pbteach.framework.domain.search.CourseSearchParam;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-09 8:57
 **/
@RestController
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;


    @Override
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esCourseService.list(page,size,courseSearchParam);
    }

    @Override
    public Map<String, CoursePub> getall(@PathVariable("id") String id) {
        return esCourseService.getall(id);
    }

    @Override
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};
        //要传入一个数组
        List<TeachplanMediaPub> teachplanMediaPubList = esCourseService.getall(teachplanIds);
        if(teachplanMediaPubList!=null && teachplanMediaPubList.size()>0){
            return teachplanMediaPubList.get(0);
        }
        return new TeachplanMediaPub();
    }
}
