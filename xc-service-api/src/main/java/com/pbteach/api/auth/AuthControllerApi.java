package com.pbteach.api.auth;

import com.pbteach.framework.domain.ucenter.request.LoginRequest;
import com.pbteach.framework.domain.ucenter.response.JwtResult;
import com.pbteach.framework.domain.ucenter.response.LoginResult;
import com.pbteach.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by pbteach.com on 2018/7/15.
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthControllerApi {

    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest);

    @GetMapping("/userjwt")
    @ApiOperation("查询用户的jwt令牌")
    public JwtResult userjwt();

    @PostMapping("/userlogout")
    @ApiOperation("退出")
    public ResponseResult logout();
}
