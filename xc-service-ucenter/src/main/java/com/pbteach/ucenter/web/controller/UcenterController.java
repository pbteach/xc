package com.pbteach.ucenter.web.controller;

import com.pbteach.api.ucenter.UcenterControllerApi;
import com.pbteach.framework.domain.ucenter.ext.XcUserExt;
import com.pbteach.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 9:04
 **/
@RestController
public class UcenterController implements UcenterControllerApi {

    @Autowired
    UserService userService;
    @Override
    public XcUserExt getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }
}
