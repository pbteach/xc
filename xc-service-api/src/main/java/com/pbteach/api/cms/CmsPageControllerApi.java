package com.pbteach.api.cms;

import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.request.QueryPageRequest;
import com.pbteach.framework.domain.cms.response.CmsPageResult;
import com.pbteach.framework.domain.cms.response.CmsPostPageResult;
import com.pbteach.framework.domain.cms.response.GenerateHtmlResult;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pbteach.com on 2018/6/23.
 */
@Api(value="cms页面管理的接口",description="cms页面管理的接口，提供页面添加、删除、修改、查询操作")
public interface CmsPageControllerApi {
    final String API_PRE = "/cms/page";
    //分页查询页面
    @ApiOperation(value="分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",paramType="path"),
            @ApiImplicitParam(name="size",value="每页记录数",paramType="path")

    })
    @GetMapping(API_PRE+"/list/{page}/{size}")
    public QueryResponseResult<CmsPage> findList(@PathVariable("page") int page,
                                                 @PathVariable("size") int size,
                                                 QueryPageRequest queryPageRequest);

    @ApiOperation(value="新增页面")
    @PostMapping(API_PRE+"/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage);

    @ApiOperation(value="保存页面，不存在页面要添加，已存在页面则更新")
    @PostMapping(API_PRE+"/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage);

    //根据id查询页面
    @ApiOperation(value="根据id查询页面")
    @GetMapping(API_PRE+"/get/{id}")
    public CmsPageResult  findById(@PathVariable("id") String id);
    //更新页面
    @ApiOperation(value="更新页面")
    @PutMapping(API_PRE+"/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage);

    @ApiOperation(value="删除页面")
    @DeleteMapping(API_PRE+"/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id);

    //生成静态化文件
    @ApiOperation(value="静态化页面")
    @PostMapping(API_PRE+"/generateHtml/{pageId}")
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId);

    @ApiOperation(value="查询静态化页面内容")
    @GetMapping(API_PRE+"/getHtml/{pageId}")
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId);

    @ApiOperation(value="页面发布")
    @PostMapping(API_PRE+"/postPage/{pageId}")
    public ResponseResult postPage(@PathVariable("pageId") String pageId);

    @ApiOperation(value="一键页面发布")
    @PostMapping(API_PRE+"/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);

}
