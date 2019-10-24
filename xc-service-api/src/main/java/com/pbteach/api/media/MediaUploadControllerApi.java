package com.pbteach.api.media;

import com.pbteach.framework.domain.media.response.CheckChunkResult;
import com.pbteach.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by pbteach.com on 2018/7/11.
 */
public interface MediaUploadControllerApi {
    final String API_PRE = "/media/upload";

    @ApiOperation(value="文件上传前注册")
    @PostMapping(API_PRE + "/register")
    public ResponseResult register(
                             @RequestParam("fileMd5") String fileMd5,
                             @RequestParam("fileName") String fileName,
                             @RequestParam("fileSize") String fileSize,
                             @RequestParam("mimetype") String mimetype,
                             @RequestParam("fileExt") String fileExt
                             );


    @ApiOperation(value="分块文件上传前的检测")
    @PostMapping(API_PRE + "/checkchunk")
    public CheckChunkResult checkchunk(@RequestParam("fileMd5") String fileMd5,
               @RequestParam("chunk") String chunk,
               @RequestParam("chunkSize") String chunkSize);

    @ApiOperation(value="上传分块文件")
    @PostMapping(API_PRE + "/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file,
              @RequestParam("fileMd5") String fileMd5,
              @RequestParam("chunk") String chunk);

    @ApiOperation(value="合并文件")
    @PostMapping(API_PRE + "/mergechunks")
    public ResponseResult mergechunks(@RequestParam("fileMd5") String fileMd5,
                       @RequestParam("fileName") String fileName,
                       @RequestParam("fileSize") Long fileSize,
                       @RequestParam("mimetype") String mimetype,
                       @RequestParam("fileExt")  String fileExt);

}
