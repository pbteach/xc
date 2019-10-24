package com.pbteach.govern.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 12:22
 **/
//@Component
public class LoginFilterTest extends ZuulFilter {
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
        return false;
    }

    @Override
    public Object run() {
        //过虑器内容
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //不再向下执行
//        requestContext.setSendZuulResponse(false);

        return null;
    }
}
