package com.pbteach.framework.domain.cms.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-06 8:53
 **/
@Data
@NoArgsConstructor//无参构造器注解
public class CmsPostPageResult extends ResponseResult {
    String pageUrl;//页面url
    public CmsPostPageResult(ResultCode resultCode,String pageUrl) {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
