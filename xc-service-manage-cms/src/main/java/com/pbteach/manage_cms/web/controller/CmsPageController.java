package com.pbteach.manage_cms.web.controller;

import com.pbteach.api.cms.CmsPageControllerApi;
import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.request.QueryPageRequest;
import com.pbteach.framework.domain.cms.response.CmsPageResult;
import com.pbteach.framework.domain.cms.response.CmsPostPageResult;
import com.pbteach.framework.domain.cms.response.GenerateHtmlResult;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 12:18
 **/
@RestController
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    CmsPageService cmsPageService;

    @Override
    public QueryResponseResult<CmsPage>  findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page,size,queryPageRequest);
    }

    @Override
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }


    @Override
    public CmsPageResult findById(@PathVariable("id") String id) {
        return cmsPageService.getById(id);
    }

    @Override
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }

    @Override
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }

    @Override
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId) {
        return cmsPageService.generateHtml(pageId);
    }

    @Override
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId) {
        return cmsPageService.getHtml(pageId);
    }

    @Override
    public ResponseResult postPage(@PathVariable("pageId") String pageId) {
        return cmsPageService.postpage(pageId);
    }

    @Override
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return cmsPageService.postPageQuick(cmsPage);
    }
}
