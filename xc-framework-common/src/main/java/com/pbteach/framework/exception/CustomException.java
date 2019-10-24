package com.pbteach.framework.exception;

import com.pbteach.framework.model.response.ResultCode;

/**
 * 自定义异常类型，包括resultCode
 * @author Administrator
 * @version 1.0
 * @create 2018-06-26 11:56
 **/
public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return resultCode;
    }

}
