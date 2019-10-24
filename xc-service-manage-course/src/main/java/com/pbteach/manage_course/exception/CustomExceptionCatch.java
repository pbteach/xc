package com.pbteach.manage_course.exception;

import com.pbteach.framework.exception.ExceptionCatch;
import com.pbteach.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-18 10:27
 **/
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    //指定本系统中的异常类型所对应的错误代码
    static{
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
        //扩展课程管理自己的异常类型所对应的错误代码...
    }
}
