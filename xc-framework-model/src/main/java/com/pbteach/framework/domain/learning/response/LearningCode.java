package com.pbteach.framework.domain.learning.response;

import com.pbteach.framework.model.response.ResultCode;
import lombok.ToString;


@ToString
public enum LearningCode implements ResultCode {
    LEARNING_GETMEDIA_NOTFOUNDVIDEOURL(false,26001,"无法获取该课程的视频地址！");
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
