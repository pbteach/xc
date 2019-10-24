package com.pbteach.api.course;

import com.pbteach.framework.domain.course.CourseBase;
import com.pbteach.framework.domain.course.CoursePic;
import com.pbteach.framework.domain.course.Teachplan;
import com.pbteach.framework.domain.course.TeachplanMedia;
import com.pbteach.framework.domain.course.ext.CourseInfo;
import com.pbteach.framework.domain.course.ext.CourseView;
import com.pbteach.framework.domain.course.ext.TeachplanNode;
import com.pbteach.framework.domain.course.request.CourseListRequest;
import com.pbteach.framework.domain.course.response.AddCourseResult;
import com.pbteach.framework.domain.course.response.CoursePublishResult;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pbteach.com on 2018/6/23.
 */
@Api(value="课程管理的接口",description="课程管理的接口，提供课程添加、删除、修改、查询操作")
public interface CourseControllerApi {
    final String API_PRE = "/course";

    //分页查询课程列表
    @GetMapping(API_PRE+"/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page")int page,
                          @PathVariable("size")int size,
                          CourseListRequest courseListRequest);

    //新增课程
    @PostMapping(API_PRE+"/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase);

    //查询课程计划
    @GetMapping(API_PRE+"/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId);

    //添加课程计划
    @PostMapping(API_PRE+"/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan);

    //保存图片地址到course_pic
    @PostMapping(API_PRE+"/coursepic/add")
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(@RequestParam("courseId")String courseId,@RequestParam("pic")String pic);

    //根据课程id 查询图片
    @GetMapping(API_PRE+"/coursepic/list/{courseId}")
    @ApiOperation("获取课程图片")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId);

    @DeleteMapping(API_PRE+"/coursepic/delete")
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(@RequestParam("courseId")String courseId);

    //查询课程全部信息用于静态化
    @ApiOperation("课程视图查询")
    @GetMapping(API_PRE+"/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id);

    //课程预览接口
    @PostMapping(API_PRE+"/preview/{id}")
    @ApiOperation("预览课程")
    public CoursePublishResult preview(@PathVariable("id") String id);

    //课程发布接口
    @PostMapping(API_PRE+"/publish/{id}")
    @ApiOperation("发布课程")
    public CoursePublishResult publish(@PathVariable("id") String id);

    @ApiOperation("保存媒资信息")
    @PostMapping(API_PRE+"/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia);
}
