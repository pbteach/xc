package com.pbteach.manage_course.web.controller;

import com.pbteach.api.course.CourseControllerApi;
import com.pbteach.framework.domain.course.CourseBase;
import com.pbteach.framework.domain.course.CoursePic;
import com.pbteach.framework.domain.course.Teachplan;
import com.pbteach.framework.domain.course.TeachplanMedia;
import com.pbteach.framework.domain.course.ext.CourseInfo;
import com.pbteach.framework.domain.course.ext.CourseView;
import com.pbteach.framework.domain.course.ext.TeachplanNode;
import com.pbteach.framework.domain.course.request.CourseListRequest;
import com.pbteach.framework.domain.course.response.AddCourseResult;
import com.pbteach.framework.domain.course.response.CoursePublishResult;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.utils.XcOauth2Util;
import com.pbteach.framework.web.BaseController;
import com.pbteach.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 11:51
 **/
@RestController
public class CourseController extends BaseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    //指定拥有哪些权限可以访问此方法
    @PreAuthorize("hasAuthority('course_find_list')")
    @Override
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {
        //使用工具类获取当前用户
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        //当前用户
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        //当前用户的所属公司Id
        String company_id  =userJwt.getCompanyId();
        return courseService.findCourseList(company_id,page,size,courseListRequest);
    }

    @Override
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.add(courseBase);
    }


//    @PreAuthorize("hasAuthority('teachplan_find_list')")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.saveCoursePic(courseId,pic);
    }

//    @PreAuthorize("hasAuthority('coursepic_find_list')")
    @Override
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursepicList(courseId);
    }

    @Override
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCoruseView(id);
    }

    @Override
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    public CoursePublishResult publish(@PathVariable("id") String id) {
        //发布课程
        return courseService.publish(id);
    }

    @Override
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }


}
