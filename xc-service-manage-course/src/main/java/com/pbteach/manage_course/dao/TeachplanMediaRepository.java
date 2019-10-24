package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pbteach.com on 2018/6/30.
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia,String> {

    //根据课程id查询课程计划媒资信息
    List<TeachplanMedia> findByCourseId(String courseId);
}
