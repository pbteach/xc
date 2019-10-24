package com.pbteach.manage_course.dao;

import com.github.pagehelper.Page;
import com.pbteach.framework.domain.course.CourseBase;
import com.pbteach.framework.domain.course.ext.CourseInfo;
import com.pbteach.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by pbteach.com on 2018/6/30.
 */
@Mapper
public interface CourseBaseMapper {
    CourseBase findCourseBaseById(String id);
    //分页查询课程列表
    Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}
