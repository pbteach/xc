package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pbteach.com on 2018/6/30.
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {

}
