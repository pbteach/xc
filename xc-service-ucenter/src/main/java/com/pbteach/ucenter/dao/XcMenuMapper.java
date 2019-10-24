package com.pbteach.ucenter.dao;

import com.pbteach.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XcMenuMapper {
    //查询某个用户的权限
    List<XcMenu> selectPermissionByUserId(String userId);
}
