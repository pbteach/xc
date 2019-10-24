package com.pbteach.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.domain.media.response.CheckChunkResult;
import com.pbteach.framework.domain.media.response.MediaCode;
import com.pbteach.framework.exception.ExceptionCast;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.manage_media.config.RabbitMQConfig;
import com.pbteach.manage_media.dao.MediaFileRepository;
import com.pbteach.manage_media.web.controller.MediaUploadController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-11 11:45
 **/
@Service
public class MediaUploadService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadController.class);

    @Autowired
    MediaFileRepository mediaFileRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;
    //视频处理路由
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;


    /**
     * 得到上传文件的路径
     * 目录路径：
     * 1/md5第一个字符
     * 2/md5第二个字符
     * 3/md5本身
     * 文件名称=md5.扩展名
     * @param fileMd5
     * @return
     */

    public String getFilePath(String fileMd5,String fileExt){
        return  uploadPath+ fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 + "." +fileExt;
    }
    //得到上传文件的目录的路径
    public String getFileFolderPath(String fileMd5){
        return  uploadPath+ fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/";
    }
    //得到分块文件的目录
    public String getChunkFileFolderPath(String fileMd5){
        return  uploadPath+ fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/"+"chunk" +"/";
    }


    //文件上传前的注册（上传前准备工作）
    public ResponseResult register(String fileMd5, String fileName, String fileSize, String mimetype, String fileExt) {
        //检查文件是否存，1、在物理磁盘上是否存在，2：在数据库是否存在，两者都存在说明此文件已存在
        //1、在物理磁盘上是否存在
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //在物理磁盘上是否存在
        boolean exists = file.exists();

        //2：判断在数据库是否存在
        MediaFile mediaFile = mediaFileRepository.findOne(fileMd5);
        if(exists && mediaFile!=null){
            //此文件已存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //得到上传文件的目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        //如果目录不存在要创建
        File fileFolder =new File(fileFolderPath);
        if(!fileFolder.exists()){
            //创建这些目录
            fileFolder.mkdirs();
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    //检查分块文件是否存在，直接检查分块文件在物理磁盘是否存在
    public CheckChunkResult checkchunk(String fileMd5, String chunk, String chunkSize) {
        //得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunk;
        File chunkFile = new File(chunkFilePath);
        if(chunkFile.exists()){//已经存在
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,true);
        }else{//未存在
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,false);
        }
    }

    //上传分块文件
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, String chunk) {

        //得到分块文件的目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolderFile = new File(chunkFileFolderPath);
        //如果块文件所在目录不存在则自动创建
        if(!chunkFileFolderFile.exists()){
            chunkFileFolderFile.mkdirs();
        }
        //得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunk;
        //分块文件
        File chunkFile = new File(chunkFilePath);
        //输出流
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("upload chunk file fail:{}",e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //合并文件
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {

        //得到分块文件的目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolderFile = new File(chunkFileFolderPath);
        //得到合并的文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);
        if(mergeFile.exists()){
            //删除原来上传一部分的合并的文件
            mergeFile.delete();
        }

        if(!mergeFile.exists()){
            try {
                //创建一个新的空文件
                boolean newFile = mergeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                ExceptionCast.cast(MediaCode.MERGE_FILE_CREATE_FAIL);
            }
        }
        //向合并后的文件写数据
        RandomAccessFile raf_write = null;
        try {
            raf_write = new RandomAccessFile(mergeFile,"rw");//读写权限
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File[] chunkFiles = chunkFileFolderFile.listFiles();
        //将数据转成list
        List<File> fileList = Arrays.asList(chunkFiles);
        //需要对chunkFiles分块文件排序，按照升序排
        Collections.sort(fileList, new Comparator<File>() {
            //比较器
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;//升序
                }
                return -1;
            }
        });
        //开始合并
        byte[] b = new byte[1024];
        try {
            for(File chunkFile:fileList){
                //从块文件中读
                RandomAccessFile raf_read  = new RandomAccessFile(chunkFile,"r");//r读
                int len = -1;
                while((len = raf_read.read(b))!=-1){
                    //向合并后的文件写
                    raf_write.write(b,0,len);
                }
                raf_read.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }finally {
            try {
                raf_write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            InputStream mergeFileInputStream = new FileInputStream(mergeFile);
            //对文件进行校验，通过比较md5值
            String newFileMd5 = DigestUtils.md5Hex(mergeFileInputStream);
            if(!fileMd5.equalsIgnoreCase(newFileMd5)){
                //校验失败
                ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //校验失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //将文件入库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileSize(fileSize);
        mediaFile.setFileName(fileMd5 +"." +fileExt);//文件名称
        String filePath_l = fileMd5.substring(0,1) + "/"+ fileMd5.substring(1,2) + "/" +fileMd5+"/";
        mediaFile.setFilePath(filePath_l);
        mediaFile.setFileType(fileExt);
        mediaFile.setMimeType(mimetype);
        mediaFile.setUploadTime(new Date());

        MediaFile save = mediaFileRepository.save(mediaFile);


        //上传文件成功，向mq发送视频处理的消息
        sendProceeVide(save.getFileId());

        return new ResponseResult(CommonCode.SUCCESS);
    }
    //向mq发送视频处理消息
    private void sendProceeVide(String mediaId){
        //构建消息 ｛“mediaId”:XXX｝
        Map<String,String> messageMap = new HashMap<>();
        messageMap.put("mediaId",mediaId);
        String message = JSON.toJSONString(messageMap);
        //String exchange, String routingKey, Object object
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,message);
    }
}
