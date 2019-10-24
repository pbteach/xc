package com.pbteach.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 12:22
 **/
//@Component
public class LoginFilterTest2 extends ZuulFilter {


    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(200);
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String s = JSON.toJSONString(responseResult);
            requestContext.setResponseBody(s);
            response.setContentType("application/json;charset=utf-8");
        }
        return null;
    }
}
