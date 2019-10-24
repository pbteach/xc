package com.pbteach.manage_cms.dao;

import com.pbteach.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/6/23.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称、站点id、页面web访问路径查询，如果查询
    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId,String pageName,String pageWebPath);

}
