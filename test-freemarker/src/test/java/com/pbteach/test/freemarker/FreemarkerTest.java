package com.pbteach.test.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-27 9:50
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {
    @Autowired
    RestTemplate restTemplate;

    // 基于模板静态化轮播图页面
    @Test
    public void testGenerateBannerHtml() throws IOException, TemplateException {
        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //加载模板
        //选指定模板路径,classpath下templates下
        //得到classpath路径
        String classpath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classpath+"/templates/"));
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = configuration.getTemplate("index_banner.ftl");

        //准备数据
        Map<String,Object> map = new HashMap<>();
        //远程获取到模型数据
        String dataUrl = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map banner = forEntity.getBody();
        System.out.println(banner);
        //设置到freemarker 数据模型中
        map.put("banner",banner);

        //静态化
        //参数1：模板，参数2：数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        //将静态化内容输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        //输出流
        FileOutputStream outputStream =new FileOutputStream("e:/index_banner.html");
        IOUtils.copy(inputStream,outputStream);

    }

    // 基于模板进行测试静态化
    @Test
    public void testGenerateHtmlByTemplate() throws IOException, TemplateException {
        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //加载模板
        //选指定模板路径,classpath下templates下
        //得到classpath路径
        String classpath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classpath+"/templates/"));
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = configuration.getTemplate("test2.ftl");

        //准备数据
        Map<String,Object> map = new HashMap<>();
        map.put("name","黑马程序员");

        //静态化
        //参数1：模板，参数2：数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        //将静态化内容输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        //输出流
        FileOutputStream outputStream =new FileOutputStream("e:/test2.html");
        IOUtils.copy(inputStream,outputStream);

    }

    // 基于字符串进行测试静态化
    @Test
    public void testGenerateHtmlByHtml() throws IOException, TemplateException {
        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());

        //模板内容
        String templateContent="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Hello World!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hello ${name}!\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        //加载模板
        //使用模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //加载上边templateContent的内容作为模板
        //参数1：给模板起一个名称，参数2：模板内容
        stringTemplateLoader.putTemplate("test2",templateContent);
        //将模板加载器设置到configuration
        configuration.setTemplateLoader(stringTemplateLoader);
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = configuration.getTemplate("test2");

        //准备数据
        Map<String,Object> map = new HashMap<>();
        map.put("name","黑马程序员");

        //静态化
        //参数1：模板，参数2：数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        //将静态化内容输出到文件中
        InputStream inputStream = IOUtils.toInputStream(content);
        //输出流
        FileOutputStream outputStream =new FileOutputStream("e:/test2.html");
        IOUtils.copy(inputStream,outputStream);

    }
}
