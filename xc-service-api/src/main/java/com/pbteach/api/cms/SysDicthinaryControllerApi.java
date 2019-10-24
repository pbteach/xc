package com.pbteach.api.cms;

import com.pbteach.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by pbteach.com on 2018/7/2.
 */
@Api(value = "数据字典接口",description = "提供数据字典接口的管理、查询功能")
public interface SysDicthinaryControllerApi {
    final String API_PRE = "/sys/dictionary";

    //查询
    @GetMapping(API_PRE+"/get/{type}")
    @ApiOperation(value="数据字典查询接口")
    public SysDictionary getByType(@PathVariable("type") String type);

    //添加

    //删除

    //修改
}
