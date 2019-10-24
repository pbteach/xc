package com.pbteach.manage_cms.web.controller;

import com.pbteach.framework.web.BaseController;
import com.pbteach.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 12:18
 **/
@Controller
public class CmsPagePreviewController extends BaseController{
    @Autowired
    CmsPageService cmsPageService;

    //接收到页面id
    @RequestMapping(value="/cms/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable("pageId")String pageId){

        //调用service根据页面得到静态化内容
        String content = cmsPageService.generateHtmlByPageId(pageId);

        //使用response将静态化内容响应到浏览器
        ServletOutputStream outputStream = null;
        try {
            outputStream  = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-type","text/html;charset=utf-8");
        //输出静态化内容
        try {
            outputStream.write(content.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
