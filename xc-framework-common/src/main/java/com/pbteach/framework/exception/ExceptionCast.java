package com.pbteach.framework.exception;

import com.pbteach.framework.model.response.ResultCode;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-26 11:57
 **/
public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
