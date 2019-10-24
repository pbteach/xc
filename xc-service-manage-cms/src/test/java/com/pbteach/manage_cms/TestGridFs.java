package com.pbteach.manage_cms;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 11:59
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGridFs {

    @Autowired
    GridFsTemplate gridFsTemplate;

    //存储文件
    @Test
    public void testStore() throws FileNotFoundException {
        //输入流
        FileInputStream inputStream =new FileInputStream(new File("e:/index_banner.ftl"));
        //存储文件
        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, "轮播图文件01");
        //文件id,，此id是fs.files集合的主键，通过此文件id查询fs.chunks集合files_id字段，得到文件块信息
        String fileId = gridFSFile.getId().toString();
        System.out.println(fileId);

    }

    //查询文件
    @Test
    public void testGetFile() throws IOException {

        //查询文件
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5b3308ee80fffa23c0e44591")));
        InputStream inputStream = gridFSDBFile.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File("e:/banner.html"));
        IOUtils.copy(inputStream,outputStream);

        //删除文件
//        gridFsTemplate.delete();
    }



}
