package com.pbteach.ucenter.dao;

import com.pbteach.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {

    //根据用户id查询所属企业id
    XcCompanyUser findByUserId(String userId);
}
