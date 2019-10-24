package com.pbteach.framework.domain.cms.request;

import com.pbteach.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest extends RequestData {
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    //页面ID
    @ApiModelProperty("页面ID")
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;

}
