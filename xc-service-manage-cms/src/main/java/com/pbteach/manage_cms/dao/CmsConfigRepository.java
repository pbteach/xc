package com.pbteach.manage_cms.dao;

import com.pbteach.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/6/23.
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
