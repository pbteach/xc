package com.pbteach.api.fs;

import com.pbteach.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by pbteach.com on 2018/7/3.
 */

@Api(value = "文件管理",description = "文件管理 ，提供文件的上传、删除、查询等服务")
public interface FileSystemControllerApi {
    final String  API_PRE = "/filesystem";
    @PostMapping(API_PRE+"/upload")
    public UploadFileResult upload(@RequestParam(value="file",required = true) MultipartFile file,
                  @RequestParam(value="businesskey",required = false) String businesskey,
                  @RequestParam(value="filetag",required = false) String filetag,
                  @RequestParam(value="metadata",required = false) String metadata
                  );
}
