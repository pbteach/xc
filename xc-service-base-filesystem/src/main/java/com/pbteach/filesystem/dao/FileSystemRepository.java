package com.pbteach.filesystem.dao;

import com.pbteach.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/7/3.
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
