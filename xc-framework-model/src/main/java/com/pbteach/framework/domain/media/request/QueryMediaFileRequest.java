package com.pbteach.framework.domain.media.request;

import com.pbteach.framework.model.request.RequestData;
import lombok.Data;

@Data
public class QueryMediaFileRequest extends RequestData {

    //文件原始名称
    private String fileOriginalName;
    //处理状态
    private String processStatus;
    //标签
    private String tag;
}
