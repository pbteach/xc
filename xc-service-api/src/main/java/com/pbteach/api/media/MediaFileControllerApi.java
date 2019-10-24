package com.pbteach.api.media;

import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.domain.media.request.QueryMediaFileRequest;
import com.pbteach.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by pbteach.com on 2018/7/12.
 */
@Api(value = "媒体文件管理",description = "媒体文件管理接口",tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {
    final String API_PRE = "/media/file";

    @GetMapping(API_PRE+"/list/{page}/{size}")
    @ApiOperation("查询文件列表")
    public QueryResponseResult<MediaFile> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) ;


}
