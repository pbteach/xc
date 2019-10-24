package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pbteach.com on 2018/6/30.
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub,String> {

    //根据课程删除记录
    int deleteByCourseId(String courseId);

}
