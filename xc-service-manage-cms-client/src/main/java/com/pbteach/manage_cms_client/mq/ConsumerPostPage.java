package com.pbteach.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.rabbitmq.client.Channel;
import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.CmsSite;
import com.pbteach.manage_cms_client.dao.CmsPageRepository;
import com.pbteach.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 9:14
 **/
@Component
public class ConsumerPostPage {

    private static final  Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    //接收页面发布的消息,从配置中注入队列名称
    @RabbitListener(queues={"${xuecheng.mq.queue}"})
    public void postPage(String msg,Message message,Channel channel){
        //解析消息
        Map msgMap = null;
        try {
            msgMap = JSON.parseObject(msg, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("receive postpage msg,parse message error:{}",e.getMessage());
        }
        //从消息中得到页面id
        String pageId = (String) msgMap.get("pageId");
        if(StringUtils.isEmpty(pageId)){
            LOGGER.error("in message not exists pageid");
            return ;
        }

        //查询数据库得到页面信息
        CmsPage cmsPage = cmsPageRepository.findOne(pageId);
        //页面所属站点
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = cmsSiteRepository.findOne(siteId);

        //从页面信息中得到静态文件id
        String htmlFileId = cmsPage.getHtmlFileId();
        if(StringUtils.isEmpty(htmlFileId)){
            LOGGER.error("page htmlfileId is null");
            return ;
        }

        //查询GridFS
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        //文件输入流
        InputStream inputStream = gridFSDBFile.getInputStream();
        //文件输出流
        //得到文件输出路径
        String filePath = cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        FileOutputStream outputStream = null;
        try {
            outputStream  = new FileOutputStream(new File(filePath));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("page html file path Not Found:{}",filePath);
        }

        try {
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("output page html error:{}",e.getMessage());
        }


    }
}
