package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pbteach.com on 2018/6/30.
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
