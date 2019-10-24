package com.pbteach.manage_cms_client.dao;

import com.pbteach.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/6/23.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

}
