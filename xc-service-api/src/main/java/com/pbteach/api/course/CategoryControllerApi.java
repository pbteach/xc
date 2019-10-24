package com.pbteach.api.course;

import com.pbteach.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pbteach.com on 2018/6/30.
 */
@RequestMapping("/category")
@Api(value = "课程分类管理",description = "课程分类管理",tags = {"课程分类管理"})
public interface CategoryControllerApi {

    @GetMapping("/list")
    @ApiOperation("查询分类")
    public CategoryNode findList();

}