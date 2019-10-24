package com.pbteach.manage_cms_client.dao;

import com.pbteach.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/6/23.
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
    
}
