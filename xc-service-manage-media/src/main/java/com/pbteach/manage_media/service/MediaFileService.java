package com.pbteach.manage_media.service;

import com.pbteach.framework.domain.media.MediaFile;
import com.pbteach.framework.domain.media.request.QueryMediaFileRequest;
import com.pbteach.framework.model.response.CommonCode;
import com.pbteach.framework.model.response.QueryResponseResult;
import com.pbteach.framework.model.response.QueryResult;
import com.pbteach.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 10:55
 **/
@Service
public class MediaFileService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    //分页查询mediaFile记录
    public QueryResponseResult<MediaFile> findList2(int page,int size,QueryMediaFileRequest queryMediaFileRequest){
        if(queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }

        //查询条件对象
        MediaFile mediaFile = new MediaFile();
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }

        if(StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }



        //拼接查询条件对象
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                                    .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())//模糊匹配
                                    .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains());

        //查询条件example对象
        Example<MediaFile> example = Example.of(mediaFile,exampleMatcher);

        //分页对象
        if(page<=0){
            page = 1;
        }
        //page从0开始
        page = page -1;
        if(size<=0){
            size = 15;
        }
        Pageable pageable = new PageRequest(page,size);
        //分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        //获取总记录
        long totalElements = all.getTotalElements();
        //获取list
        List<MediaFile> list = all.getContent();

        //查询结果集
        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setTotal(totalElements);
        queryResult.setList(list);
        QueryResponseResult<MediaFile> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);

        return queryResponseResult;
    }
}
