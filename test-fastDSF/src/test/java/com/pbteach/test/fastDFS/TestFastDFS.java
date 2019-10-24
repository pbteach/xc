package com.pbteach.test.fastDFS;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-03 9:42
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    //测试上传
    @Test
    public void testUpload() throws IOException, MyException {
        //加载配置文件（trackerserver地址）
        ClientGlobal.initByProperties("config/fastdfs-client.properties");

        //tracker client，请求tracker得到storage地址
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker server
        TrackerServer trackerServer = trackerClient.getConnection();
        //得到strorage的地址
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //strage client用来上传文件
        //构造参数 TrackerServer trackerServer, StorageServer storageServer
        StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
        //通过storageClient1上传文件
        //参数：String local_filename, String file_ext_name, NameValuePair[] meta_list
        /**
         * 1、本地文件路径
         * 2、文件扩展名
         * 3、meta_list文件元信息
         */
        //上传文件，成功后返回文件id（文件地址）
        String fileId = storageClient1.upload_file1("d:/logo.png", "png", null);
        System.out.println(fileId);

    }

    //测试下载
    @Test
    public void testDownload() throws IOException, MyException {
        //加载配置文件（trackerserver地址）
        ClientGlobal.initByProperties("config/fastdfs-client.properties");

        //tracker client，请求tracker得到storage地址
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker server
        TrackerServer trackerServer = trackerClient.getConnection();
        //得到strorage的地址
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //strage client用来上传文件
        //构造参数 TrackerServer trackerServer, StorageServer storageServer
        StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
       //下载文件
        byte[] bytes = storageClient1.download_file1("group1/M00/00/01/wKhlQVs7R7iAZjyLAAAawU0ID2Q769.png");
//        storageClient1.delete_file1()删除
//        FileInfo fileInfo = storageClient1.query_file_info1(""); 查询
        FileOutputStream outputStream  =new FileOutputStream(new File("d:/logo1.png"));
        outputStream.write(bytes);
        outputStream.close();

    }


}
