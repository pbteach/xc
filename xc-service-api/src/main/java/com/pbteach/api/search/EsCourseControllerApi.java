package com.pbteach.api.search;


import com.pbteach.framework.domain.course.CoursePub;
import com.pbteach.framework.domain.course.TeachplanMediaPub;
import com.pbteach.framework.domain.search.CourseSearchParam;
import com.pbteach.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Api(value="课程搜索接口",description="课程搜索接口")
public interface EsCourseControllerApi {

    final String API_PRE = "/search/course";
    //根据配置信息id查询配置信息
    @GetMapping(API_PRE+"/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,
                @PathVariable("size") int size,
                CourseSearchParam courseSearchParam);

    @GetMapping(value=API_PRE+"/getall/{id}")
    @ApiOperation("根据id查询课程信息")
    public Map<String,CoursePub> getall(@PathVariable("id") String id);

    @GetMapping(value=API_PRE+"/getmedia/{teachplanId}")
    @ApiOperation("根据课程计划查询媒资信息")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);

}
