package com.pbteach.manage_course;

import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.response.CmsPageResult;
import com.pbteach.framework.domain.cms.response.CmsPostPageResult;
import com.pbteach.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-05 9:50
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {
    @Autowired
    CmsPageClient cmsPageClient;


    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    @Test
    public void testRibbon(){
//        for(int i=0;i<3;i++){
            CmsPageResult cmsPage = cmsPageClient.findById("5a795ac7dd573c04508f3a56");
            System.out.println(cmsPage);
//        }

    }
    //单元测试一键发布页面的接口
    @Test
    public void testPostPageQuick(){
        //准备页面信息
        //课程id
        String id = "4028e581617f945f01617f9dabc40000";
        //请求cms添加课程页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页面的名称为 "课程id.html"
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase("Bootstrap开发框架");//页面别名就是课程名称
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());

        //远程调用cms进行一键发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        System.out.println(cmsPostPageResult);
    }
}
