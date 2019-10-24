package com.pbteach.framework.domain.course.request;

import com.pbteach.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/4/13.
 */
@Data
@ToString
public class CourseListRequest extends RequestData {
    //公司Id
    private String companyId;
}
