package com.pbteach.manage_cms.web.controller;

import com.pbteach.api.cms.CmsConfigControllerApi;
import com.pbteach.framework.domain.cms.response.CmsConfigResult;
import com.pbteach.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 12:18
 **/
@RestController
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired
    CmsConfigService cmsConfigService;
    @Override
    public CmsConfigResult getmodel(@PathVariable("id") String id) {
        return cmsConfigService.getConfigById(id);
    }
}
