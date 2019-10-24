package com.pbteach.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.framework.domain.cms.CmsSite;
import com.pbteach.framework.domain.cms.CmsTemplate;
import com.pbteach.framework.domain.cms.request.QueryPageRequest;
import com.pbteach.framework.domain.cms.response.CmsCode;
import com.pbteach.framework.domain.cms.response.CmsPageResult;
import com.pbteach.framework.domain.cms.response.CmsPostPageResult;
import com.pbteach.framework.domain.cms.response.GenerateHtmlResult;
import com.pbteach.framework.exception.ExceptionCast;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.QueryResult;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.manage_cms.config.RabbitMQConfig;
import com.pbteach.manage_cms.dao.CmsPageRepository;
import com.pbteach.manage_cms.dao.CmsSiteRepository;
import com.pbteach.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 12:16
 **/
@Service
public class CmsPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPageService.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    //分页查询cmspage
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;//为了适应mongodb的接口
        if (size <= 0) {
            size = 20;
        }

        //条件匹配器,默认就是精确匹配
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());//指定模糊匹配,String propertyPath, ExampleMatcher.GenericPropertyMatcher genericPropertyMatcher
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //判断如果传入站点id，将站点id赋值到cmsPage查询值对象
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置页面别名查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
//        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");//查询该站点下的页面
//        cmsPage.setPageAliase("页面");
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //分页参数
        Pageable pageable = new PageRequest(page, size);
        //执行分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        //数据列表集合
        List<CmsPage> content = all.getContent();
        //总记录数
        long total = all.getTotalElements();
        //定义QueryResult
        QueryResult queryResult = new QueryResult();
        queryResult.setList(content);
        queryResult.setTotal(total);
        //返回结果
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        if(cmsPage == null){
            //校验，抛出异常
        }
        //校验页面是否重复，根据页面名称、站点id、页面web访问路径判断此页面是否重复
        //根据页面名称、站点id、页面web访问路径查询，如果查询到了说明页面已存在
        CmsPage cmsPage_l = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(),
                cmsPage.getPageName(),
                cmsPage.getPageWebPath());
        if(cmsPage_l!=null){
            //抛出具体的异常。。
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTS);
        }
        //将主键设置为空
        cmsPage.setPageId(null);
        CmsPage save = cmsPageRepository.save(cmsPage);
        if(save!=null){
            //返回成功
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        //返回失败
        return new CmsPageResult(CommonCode.FAIL,null);

    }

    //根据id查询页面
    public CmsPageResult getById(String id){
        CmsPage one = cmsPageRepository.findOne(id);
        if(one!=null){
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //更新页面
    public CmsPageResult update(String id,CmsPage cmsPage){
        CmsPage one = cmsPageRepository.findOne(id);
        if(one!=null){
            //更新数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //保存dataurl
            one.setDataUrl(cmsPage.getDataUrl());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage save = cmsPageRepository.save(one);
            if(save!=null){
                return new CmsPageResult(CommonCode.SUCCESS,save);
            }
        }

        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //删除页面
    public ResponseResult delete(String  id){
        cmsPageRepository.delete(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据模板id得到模板文件内容
    private String getPageTempateContent(String templateId){
        //根据根据模板id查询cms_template表，找到templateFileId
        CmsTemplate one = cmsTemplateRepository.findOne(templateId);
        if(one == null){
            return null;
        }
        //模板文件id
        String templateFileId = one.getTemplateFileId();
        if(StringUtils.isEmpty(templateFileId)){
            return null;
        }
        //根据templateFileId从GridFS中查询模板文件
        //根据templateFileId从GridFS查询到模板内容
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        //模板文件的流对象
        InputStream inputStream = gridFSDBFile.getInputStream();
        String templateContent = null;
        try {
            templateContent = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //如果模板为空要抛出异常
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        return templateContent;
    }

    //将静态文件的内容保存到GridFS中
    public String saveHtml(String htmlContent){
        InputStream htmlInputStream = IOUtils.toInputStream(htmlContent);
        //将静态文件的内容存储到GridFS中
        GridFSFile gridFSFile = gridFsTemplate.store(htmlInputStream, "静态文件");
        //得到文件id
        String fileId = gridFSFile.getId().toString();
        return fileId;
    }

    /**
     * 根据页面生成静态化内容
     * @param pageId
     * @return
     */
    public String generateHtmlByPageId(String pageId){
        //查询页面信息
        CmsPage one = cmsPageRepository.findOne(pageId);
        if(one == null){
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        //得到模板id和dataUrl
        String dataUrl = one.getDataUrl();
        //模板id
        String templateId = one.getTemplateId();
        //模型数据
        Map map = null;
        //远程请求dataUrl获取数据
        if(StringUtils.isNotEmpty(dataUrl)){
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            map = forEntity.getBody();
        }
        //根据模板id从GridFS中查询到模板文件内容
        String templateContent = this.getPageTempateContent(templateId);

        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //加载模板
        //使用模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //加载上边templateContent的内容作为模板
        //参数1：给模板起一个名称，参数2：模板内容
        stringTemplateLoader.putTemplate("template",templateContent);
        //将模板加载器设置到configuration
        configuration.setTemplateLoader(stringTemplateLoader);
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = null;
        try {
            template = configuration.getTemplate("template");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        //准备数据
        Map<String,Object> model = new HashMap<>();
//        model.put("model",map);
        model.putAll(map);


        //静态化
        //参数1：模板，参数2：数据模型
        String content = null;
        try {
            //静态化执行
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        System.out.println(content);
        return content;

    }
    /**
     * 生成静态化文件
     * @param pageId
     * @return
     */
    public GenerateHtmlResult generateHtml(String pageId){
        //查询页面信息
        CmsPage one = cmsPageRepository.findOne(pageId);
        if(one == null){
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        //得到模板id和dataUrl
        String dataUrl = one.getDataUrl();
        //模板id
        String templateId = one.getTemplateId();
        //模型数据
        Map map = null;
        //远程请求dataUrl获取数据
        if(StringUtils.isNotEmpty(dataUrl)){
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            map = forEntity.getBody();
        }
        //根据模板id从GridFS中查询到模板文件内容
        String templateContent = this.getPageTempateContent(templateId);

        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //加载模板
        //使用模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //加载上边templateContent的内容作为模板
        //参数1：给模板起一个名称，参数2：模板内容
        stringTemplateLoader.putTemplate("template",templateContent);
        //将模板加载器设置到configuration
        configuration.setTemplateLoader(stringTemplateLoader);
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = null;
        try {
            template = configuration.getTemplate("template");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        //准备数据
        Map<String,Object> model = new HashMap<>();
        model.put("model",map);


        //静态化
        //参数1：模板，参数2：数据模型
        String content = null;
        try {
            //静态化执行
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        System.out.println(content);

        //保存静态文件内容到 GridFS
        String fileId = this.saveHtml(content);


        //将文件id存储到 cmspage
        one.setHtmlFileId(fileId);
        CmsPage save = cmsPageRepository.save(one);

        return new GenerateHtmlResult(CommonCode.SUCCESS,content);

    }

    //查询静态页面内容
    public GenerateHtmlResult getHtml(String pageId){
        //查询页面信息
        CmsPage one = cmsPageRepository.findOne(pageId);
        if(one == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTFOUND);
        }
        //得到页面静态文件id
        String htmlFileId = one.getHtmlFileId();
        String html = null;
        try {
        //从GridFS查询静态文件内容
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        InputStream inputStream = gridFSDBFile.getInputStream();

            html = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            LOGGER.error("get html from gridFS error:{}",e.getMessage());
            e.printStackTrace();
            return new GenerateHtmlResult(CommonCode.FAIL,null);

        }
        return new GenerateHtmlResult(CommonCode.SUCCESS,html);
    }

    //页面发布
    public ResponseResult postpage(String pageId){

        //静态化
        GenerateHtmlResult generateHtmlResult = this.generateHtml(pageId);
        if(!generateHtmlResult.isSuccess()){
            //静态化失败
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //得到页面信息
        CmsPage cmsPage = cmsPageRepository.findOne(pageId);
        //得到站点id，就是routingKey
        String routingKey = cmsPage.getSiteId();
        //发送的消息内容
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("pageId",pageId);
        String msg = JSON.toJSONString(msgMap);
        //向mq发消息
        //参数：String exchange, String routingKey, Object object
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_CMS_POSTPAGE,routingKey,msg);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    //页面保存功能
    public CmsPageResult save(CmsPage cmsPage) {
        //校验页面是否重复，根据页面名称、站点id、页面web访问路径判断此页面是否重复
        //根据页面名称、站点id、页面web访问路径查询，如果查询到了说明页面已存在
        CmsPage cmsPage_l = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(),
                cmsPage.getPageName(),
                cmsPage.getPageWebPath());
        if(cmsPage_l == null){
            //执行添加
            CmsPageResult add = this.add(cmsPage);
            return add;
        }else{
            //执行更新
            CmsPageResult update = this.update(cmsPage_l.getPageId(), cmsPage);
            return update;
        }


    }

    //一键发布页面
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //保存页面
        CmsPageResult cmsPageResult = this.save(cmsPage);
        if(!cmsPageResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        //新添加页面的id
        String pageId = cmsPage1.getPageId();
        //发布页面
        ResponseResult responseResult = this.postpage(pageId);
        if(!responseResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        //发布成功了，最终给调用方返回页面url
        //页面Url= cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName

        //页面所属站点id
        String siteId = cmsPage1.getSiteId();
        CmsSite cmsSite = cmsSiteRepository.findOne(siteId);
        String siteDomain = cmsSite.getSiteDomain();//站点域名
        String siteWebPath = cmsSite.getSiteWebPath();//站点根路径

        String pageWebPath = cmsPage1.getPageWebPath();//页面路径
        String pageName = cmsPage1.getPageName();//页面名称

        //页面url
        String pageUrl = siteDomain+siteWebPath+pageWebPath+pageName;
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }
}
