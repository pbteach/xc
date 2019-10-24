package com.pbteach.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-26 12:00
 **/
@ControllerAdvice//控制器增强
public class ExceptionCatch {
    private static  final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //将不可预知的异常在map中配置异常所对应的错误代码及信息
    //ImmutableMap是不可变
    private ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //用于构建ImmutableMap的数据
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //捕获不可预知异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult catchException(Exception e){
        LOGGER.error("catch exception info{}",e.getMessage());
        e.printStackTrace();
        if(EXCEPTIONS == null){
            //异常类型和错误代码的map构建成功
            EXCEPTIONS = builder.build();
        }
        //从map中找异常类型所对应的错误代码
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if(resultCode!=null){
            return new ResponseResult(resultCode);
        }
        //否则统一抛出99999异常
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    //可以捕获异常
    //ExceptionHandler和ControllerAdvice配合起来实现捕获异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch customException info{}",e.getMessage());
        e.printStackTrace();
        //处理异常
        ResultCode resultCode = e.getResultCode();

        //给用户返回ResponseResult类的信息，以json格式输出
        return new ResponseResult(resultCode);
    }
    static{

        //指定异常类型所对应的错误代码
        builder.put(org.springframework.http.converter.HttpMessageNotReadableException.class, CommonCode.INVLIDATE);
    }


}
