package com.pbteach.filesystem.web.controller;

import com.pbteach.api.fs.FileSystemControllerApi;
import com.pbteach.filesystem.service.FileSystemService;
import com.pbteach.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-03 11:23
 **/
@RestController
public class FileSystemController implements FileSystemControllerApi{
    @Autowired
    FileSystemService fileSystemService;
    @Override
    public UploadFileResult upload(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(value = "businesskey", required = false) String businesskey, @RequestParam(value = "filetag", required = false) String filetag, @RequestParam(value = "metadata", required = false) String metadata) {
        return fileSystemService.upload(file,businesskey,filetag,metadata);
    }
}
