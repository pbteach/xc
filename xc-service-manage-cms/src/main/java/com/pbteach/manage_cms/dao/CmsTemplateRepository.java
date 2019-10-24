package com.pbteach.manage_cms.dao;

import com.pbteach.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/6/23.
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

}
