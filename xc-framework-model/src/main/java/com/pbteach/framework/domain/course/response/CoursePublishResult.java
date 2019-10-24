package com.pbteach.framework.domain.course.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-05 12:16
 **/
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {
    String previewUrl;
    public CoursePublishResult(ResultCode resultCode,String previewUrl) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
