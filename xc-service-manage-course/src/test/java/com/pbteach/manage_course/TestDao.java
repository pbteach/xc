package com.pbteach.manage_course;

import com.pbteach.framework.domain.course.CourseBase;
import com.pbteach.framework.domain.course.ext.CategoryNode;
import com.pbteach.manage_course.dao.CategoryMapper;
import com.pbteach.manage_course.dao.CourseBaseMapper;
import com.pbteach.manage_course.dao.CourseBaseRepository;
import com.pbteach.manage_course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 11:07
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {

    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CourseService courseService;


    @Test
    public void testSaveMedia(){
        courseService.saveTeachplanMedia("297e7c7c62b8aa9d0162b8ab56ba0001");
        System.out.println("ddd");
    }

    @Test
    public void testJpa(){
        CourseBase courseBase = courseBaseRepository.findOne("297e7c7c62b8aa9d0162b8ab70e90002");
        System.out.println(courseBase);

    }

    @Test
    public void testMapper(){
//        CourseBase courseBase = courseBaseMapper.findCourseBaseById("297e7c7c62b8aa9d0162b8ab70e90002");
//        System.out.println(courseBase);
        //设置分页参数
//        PageHelper.startPage(1,10);
//        Page<CourseInfo> courseListPage = courseBaseMapper.findCourseListPage(null);
//        List<CourseInfo> result = courseListPage.getResult();
//        long total = courseListPage.getTotal();
//        System.out.println(result);

        CategoryNode categoryNode = categoryMapper.selectList();
        System.out.println(categoryNode);

    }
}
