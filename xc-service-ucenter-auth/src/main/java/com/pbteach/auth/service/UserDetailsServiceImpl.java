package com.pbteach.auth.service;

import com.pbteach.auth.client.UserClient;
import com.pbteach.framework.domain.ucenter.XcMenu;
import com.pbteach.framework.domain.ucenter.ext.XcUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用用户中心
        XcUserExt xcUserExt = userClient.getUserext(username);
//        XcUserExt userext = new XcUserExt();
        if(xcUserExt == null){
            //用户为空，返回null，说明用户不存在
            return null;
        }
        //根据账号查询xc_user数据库，得到用户的正确密码，
//        String password = "123";
        String password  =xcUserExt.getPassword();


        //权限标识串
//        String user_permission_string  = "course_find_list,coursepic_find_list";//多个权限中间以逗号分隔
        List<XcMenu> xcMenus = xcUserExt.getPermissions();
        List<String> permissions = new ArrayList<>();
        for(XcMenu xcMenu:xcMenus){
            //权限标识符
            String code = xcMenu.getCode();
            permissions.add(code);
        }
        //将permissions数据拼接成字符串，中间以逗号分隔
        String user_permission_string = StringUtils.join(permissions.toArray(),",");
        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));

        //将用户的相关信息加入userDetails，将来在jwt令牌中才包括加入的这些信息
        //企业id
        userDetails.setCompanyId(xcUserExt.getCompanyId());
        //头像
        userDetails.setUserpic(xcUserExt.getUserpic());
        //用户名称
        userDetails.setName(xcUserExt.getName());
        //用户类型
        userDetails.setUtype(xcUserExt.getUtype());

        //用户id
        userDetails.setId(xcUserExt.getId());

       /* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;
    }
}
