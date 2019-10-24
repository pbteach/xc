package com.pbteach.manage_cms.dao;

import com.pbteach.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/7/2.
 */
public interface SysDicthinaryRepository extends MongoRepository<SysDictionary,String> {
    //根据type查询
    public SysDictionary findBydType(String dType);
}
