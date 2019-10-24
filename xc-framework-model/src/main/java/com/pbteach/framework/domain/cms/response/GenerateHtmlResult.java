package com.pbteach.framework.domain.cms.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;

/**
 * Created by pbteach.com on 2018/3/31.
 */
@Data
public class GenerateHtmlResult extends ResponseResult {
    String html;
    public GenerateHtmlResult(ResultCode resultCode, String html) {
        super(resultCode);
        this.html = html;
    }
}
