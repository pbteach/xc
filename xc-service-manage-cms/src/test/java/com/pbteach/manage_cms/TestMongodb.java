package com.pbteach.manage_cms;

import com.pbteach.framework.domain.cms.CmsPage;
import com.pbteach.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-23 11:59
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMongodb {

    @Autowired
    CmsPageRepository cmsPageRepository;

    //查询cmspage
    @Test
    public void testFindList(){
        //查询单个记录
//        CmsPage one = cmsPageRepository.findOne("5a754adf6abb500ad05688d9");
//        System.out.println(one);

        //分页查询,<S extends T> Page<S> findAll(Example<S> var1, Pageable var2);


        //条件参数
        //条件匹配器,默认就是精确匹配
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                        .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());//指定模糊匹配,String propertyPath, ExampleMatcher.GenericPropertyMatcher genericPropertyMatcher
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");//查询该站点下的页面
        cmsPage.setPageAliase("页面");
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        //分页参数
        int page = 0;//从0开始
        int size = 10;//每页记录数
        Pageable pageable =new PageRequest(page,size);
            //执行分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        //数据列表集合
        List<CmsPage> content = all.getContent();
        System.out.println(content);
    }

}
