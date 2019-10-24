package com.pbteach.manage_course.dao;

import com.pbteach.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by pbteach.com on 2018/6/30.
 */
@Mapper
public interface CategoryMapper {
   //查询分类
    public CategoryNode selectList();
}
