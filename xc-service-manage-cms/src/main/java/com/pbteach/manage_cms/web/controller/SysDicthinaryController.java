package com.pbteach.manage_cms.web.controller;

import com.pbteach.api.cms.SysDicthinaryControllerApi;
import com.pbteach.framework.domain.system.SysDictionary;
import com.pbteach.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-02 8:49
 **/
@RestController
public class SysDicthinaryController implements SysDicthinaryControllerApi {

    @Autowired
    CmsConfigService cmsConfigService;
    @Override
    public SysDictionary getByType(@PathVariable("type") String type) {
        return cmsConfigService.findDictionaryByType(type);
    }
}
