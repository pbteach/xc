package com.pbteach.framework.domain.cms.response;

import com.pbteach.framework.domain.cms.CmsConfig;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-27 10:54
 **/
@Data
public class CmsConfigResult extends ResponseResult {
    //cms_config数据
    CmsConfig cmsConfig;
    public CmsConfigResult(ResultCode resultCode, CmsConfig cmsConfig) {
        super(resultCode);
        this.cmsConfig = cmsConfig;
    }
}
