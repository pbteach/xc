package com.pbteach.manage_media.dao;

import com.pbteach.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pbteach.com on 2018/7/11.
 */
public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
