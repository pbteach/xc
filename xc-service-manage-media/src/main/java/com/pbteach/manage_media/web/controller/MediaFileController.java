package com.pbteach.manage_media.web.controller;

import com.pbteach.api.media.MediaFileControllerApi;
import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.domain.media.request.QueryMediaFileRequest;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 10:54
 **/
@RestController
public class MediaFileController implements MediaFileControllerApi{

    @Autowired
    MediaFileService mediaFileService;

    @Override
    public QueryResponseResult<MediaFile> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findList(page,size,queryMediaFileRequest);
    }
}
