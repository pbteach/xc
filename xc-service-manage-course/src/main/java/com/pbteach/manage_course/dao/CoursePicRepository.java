package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pbteach.com on 2018/6/30.
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    //删除课程图片记录
    //如果大于0表示成功，否则失败
    public long deleteByCourseid(String courseid);
}
