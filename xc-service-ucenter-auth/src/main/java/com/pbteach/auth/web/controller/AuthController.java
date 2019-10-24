package com.pbteach.auth.web.controller;

import com.pbteach.api.auth.AuthControllerApi;
import com.pbteach.auth.service.AuthService;
import com.pbteach.framework.domain.ucenter.ext.AuthToken;
import com.pbteach.framework.domain.ucenter.ext.UserTokenStore;
import com.pbteach.framework.domain.ucenter.request.LoginRequest;
import com.pbteach.framework.domain.ucenter.response.JwtResult;
import com.pbteach.framework.domain.ucenter.response.LoginResult;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.utils.CookieUtil;
import com.pbteach.framework.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-15 11:28
 **/
@RestController
public class AuthController extends BaseController implements AuthControllerApi {

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    AuthService authService;


    @Override
    public LoginResult login(LoginRequest loginRequest) {

        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            //账号没有输入
        }


        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        String access_token = authToken.getAccess_token();
        //将令牌存储到cookie
        saveTokenToCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS,access_token);
    }

    @Override
    public JwtResult userjwt() {
        //取出cookie中的token
        String token = getTokenFormCookie();
        //根据token查询redis中jwt
        UserTokenStore userTokenStore = authService.getUserToken(token);
        //返回jwt
        return new JwtResult(CommonCode.SUCCESS,userTokenStore.getJwt_token());
    }

    @Override
    public ResponseResult logout() {

        //从cookie中取出token
        String token = getTokenFormCookie();

        //删除redis中token
        authService.deleteTokenFromRedis(token);

        //清除 cookie
        clearToken();

        return new ResponseResult(CommonCode.SUCCESS);
    }
    //清除cookie
    private void clearToken(){
        CookieUtil.addCookie(response,cookieDomain,"/","uid","",0,false);

    }

    //存储令牌到cookie
    private void saveTokenToCookie(String token){
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);

    }

    //取出 cookie中名称为uid的值
    private String getTokenFormCookie(){
        Map<String, String> stringMap = CookieUtil.readCookie(request, "uid");
        return stringMap.get("uid");

    }
}
