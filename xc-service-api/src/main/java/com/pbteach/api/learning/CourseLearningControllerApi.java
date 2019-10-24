package com.pbteach.api.learning;

import com.pbteach.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by pbteach.com on 2018/7/14.
 */
@Api(value = "录播课程学习管理",description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    final String API_PRE = "/learning/course";

    @GetMapping(API_PRE+"/getmedia/{courseId}/{teachplanId}")
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(@PathVariable String courseId,@PathVariable String teachplanId);

}
