package com.pbteach.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.pbteach.filesystem.dao.FileSystemRepository;
import com.pbteach.framework.domain.filesystem.FileSystem;
import com.pbteach.framework.domain.filesystem.response.FileSystemCode;
import com.pbteach.framework.domain.filesystem.response.UploadFileResult;
import com.pbteach.framework.exception.ExceptionCast;
import com.pbteach.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-03 11:03
 **/
@Service
public class FileSystemService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Autowired
    private FileSystemRepository fileSystemRepository;
    /**
     * xuecheng:
     * fastdfs:
     * connect_timeout_in_seconds: 5
     * network_timeout_in_seconds: 30
     * charset: UTF-8
     * tracker_servers: 192.168.101.64:22122,192.168.101.63:22122
     */
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;


    //配置fastdfs
    public void initTrackerConfig() {
        //例子：tracker_servers: 192.168.101.64:22122,192.168.101.63:22122
        String[] split = tracker_servers.split(",");
        InetSocketAddress[] trackerAddresses = new InetSocketAddress[split.length];
        int i = 0;
        for (String serverAddr : split) {
            String[] server = serverAddr.split(":");
            //参数：String hostname, int port
            //1、ip或主机，2：端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(server[0], Integer.parseInt(server[1]));
            trackerAddresses[i] = inetSocketAddress;
            i++;
        }
        try {
            ClientGlobal.initByTrackers(trackerAddresses);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        ClientGlobal.setG_charset(charset);
    }
    //上传方法

    /**
     * 业务流程：
     * 1、加载fastDFS的配置
     * 2、向文件上传到fastDFS中，得到文件id
     * 3、将文件id及文件信息存储到mongodb的xc_fs数据库中
     * 4、将新上传文件信息返回
     *
     * @param file
     * @param businesskey
     * @param filetag
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile file, String businesskey, String filetag, String metadata) {
        //配置tracker
        initTrackerConfig();
        //向fastDFS上传文件
        String fileId = this.fdfs_upload(file);
        //将文件信息存储到mongodb数据库
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileSize(file.getSize());
        fileSystem.setFileName(file.getOriginalFilename());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        //将json转成map
        try {
            if(StringUtils.isNotEmpty(metadata)){
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileSystem.setFileType(file.getContentType());


        FileSystem fileSystem1 = fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem1);

    }

    //向fastDFS上传文件
    public String fdfs_upload(MultipartFile file) {
        try {
            //tracker client，请求tracker得到storage地址
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker server
            TrackerServer trackerServer = trackerClient.getConnection();
            //得到strorage的地址
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //strage client用来上传文件
            //构造参数 TrackerServer trackerServer, StorageServer storageServer
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            //得到新文件字节
            byte[] bytes = file.getBytes();
            //得到文件扩展名
            String originalFilename = file.getOriginalFilename();//原始名称
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            String fileId = storageClient1.upload_file1(bytes, ext, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("upload to fastDFS error{}",e.getMessage());
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);

        }
       return null;
    }
}
