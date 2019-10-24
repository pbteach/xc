package com.pbteach.framework.domain.course.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/3/20.
 */
@Data
@ToString
public class AddCourseResult extends ResponseResult {
    public AddCourseResult(ResultCode resultCode,String courseid) {
        super(resultCode);
        this.courseid = courseid;
    }
    private String courseid;

}
