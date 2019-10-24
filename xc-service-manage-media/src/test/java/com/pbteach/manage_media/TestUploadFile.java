package com.pbteach.manage_media;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-11 10:38
 **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestUploadFile {

    @Test
    public void testMd5() throws IOException {
        File file = new File("D:\\BaiduNetdiskDownload\\IntelliJ.zip");
        FileInputStream fileInputStream = new FileInputStream(file);
        String newFileMd5 = DigestUtils.md5Hex(fileInputStream);
        System.out.println(newFileMd5);
    }
    //文件分块
    @Test
    public void testSplitFile() throws IOException {
        //源文件
        File sourceFile = new File("e:/ffmpeg_test/lucene.avi");
        //分块文件所在目录
        //分块文件所在路径
        String chunkFilePath = "e:/ffmpeg_test/chunks/";
        File chunkFileFolder = new File(chunkFilePath);
        if(!chunkFileFolder.exists()){
            chunkFileFolder.mkdirs();//创建块文件目录
        }
        //读取源文件对象
        RandomAccessFile raf_read  = new RandomAccessFile(sourceFile,"r");//只读
        //每个块的大小
        long  chunkSize = 1 * 1024 *1024;

        //可以得块数
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0/chunkSize);

        byte[] b = new byte[1024];
        for(int i=0;i<chunkNum;i++){
            //块文件
            File chunkFile = new File(chunkFilePath+i);//块文件的名称为序号
            //块文件的写对象
            RandomAccessFile raf_write  = new RandomAccessFile(chunkFile,"rw");//rw读写权限
            int len = -1;
            while((len = raf_read.read(b))!=-1){
                //向块文件中写
                raf_write.write(b,0,len);
                //当已经读取了块文件的大小，不再读了
                if(chunkFile.length()>=chunkSize){
                    break;
                }
            }
            raf_write.close();

        }
        raf_read.close();


    }

    //文件合并

    //文件分块
    @Test
    public void testMergeFile() throws IOException {
        //分块文件所在路径
        String chunkFilePath = "e:/ffmpeg_test/chunks/";
        File chunkFileFolder = new File(chunkFilePath);
        //创建合并后的文件
        File mergeFile = new File("e:/ffmpeg_test/lucene02.avi");
        //创建空的合并文件
        boolean newFile = mergeFile.createNewFile();
        //向合并的文件中写
        RandomAccessFile raf_write  = new RandomAccessFile(mergeFile,"rw");//rw读写权限
        //获取所有块文件
        File[] chunkFiles = chunkFileFolder.listFiles();
        //将数据转成list
        List<File> fileList = Arrays.asList(chunkFiles);
        //需要对chunkFiles分块文件排序，按照升序排
        Collections.sort(fileList, new Comparator<File>() {
            //比较器
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;//升序
                }
                return -1;
            }
        });
        byte[] b = new byte[1024];
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
        raf_write.close();

    }
}
