package com.pbteach.framework.domain.learning.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-14 11:14
 **/
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {
    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
    //媒资文件播放地址
    private String fileUrl;

}
