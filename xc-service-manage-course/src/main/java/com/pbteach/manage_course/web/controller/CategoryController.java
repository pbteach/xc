package com.pbteach.manage_course.web.controller;

import com.pbteach.api.course.CategoryControllerApi;
import com.pbteach.framework.domain.course.ext.CategoryNode;
import com.pbteach.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 11:51
 **/
@RestController
public class CategoryController implements CategoryControllerApi {
    @Autowired
    CourseService courseService;

    @Override
    public CategoryNode findList() {
        return courseService.findCategoryList();
    }
}
