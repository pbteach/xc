package com.pbteach.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.pbteach.framework.domain.ucenter.ext.UserTokenStore;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.utils.CookieUtil;
import com.pbteach.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 12:22
 **/
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    AuthService authService;
    @Override
    public String filterType() {
        //设置过虑器的类型，包括：pre（前置过虑器）,routing
        /**
         pre：请求在被路由之前执行

         routing：在路由请求时调用

         post：在routing和errror过滤器之后调用

         error：处理请求时发生错误调用

         */
        return "pre";
    }

    @Override
    public int filterOrder() {
        //设置过虑器的顺序，想让这个过虑器优先执行，将顺序值设置小一些
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回true表示此过虑器起到作用
        return true;
    }

    //校验用户身份令牌
    @Override
    public Object run() {
        //过虑器内容
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //从cookie取出token
        String token = getTokenFormCookie(request);
        if(StringUtils.isEmpty(token)){
            //拒绝访问
            access_denied();
        }

        //查询redis
        UserTokenStore userToken = authService.getUserToken(token);
        if(userToken == null){
            //拒绝访问
            access_denied();
        }

        return null;
    }
    //取出 cookie中名称为uid的值
    private String getTokenFormCookie(HttpServletRequest request){
        Map<String, String> stringMap = CookieUtil.readCookie(request, "uid");
        return stringMap.get("uid");

    }

    //拒绝访问方法
    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应的内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String responseResultString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(responseResultString);
        requestContext.getResponse().setContentType("application/json;charset=utf-8");
    }

}
