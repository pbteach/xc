package com.pbteach.manage_media.web.controller;

import com.pbteach.api.media.MediaUploadControllerApi;
import com.pbteach.framework.domain.media.response.CheckChunkResult;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-11 11:45
 **/
@RestController
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    @Override
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("fileSize") String fileSize, @RequestParam("mimetype") String mimetype, @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @Override
    public CheckChunkResult checkchunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") String chunk, @RequestParam("chunkSize") String chunkSize) {
        return mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
    }

    @Override
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") String chunk) {
        return mediaUploadService.uploadchunk(file, fileMd5, chunk);
    }

    @Override
    public ResponseResult mergechunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("fileSize") Long fileSize, @RequestParam("mimetype") String mimetype, @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.mergechunks(fileMd5, fileName, fileSize,mimetype,fileExt);
    }
}
