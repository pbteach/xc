package com.pbteach.manage_course.client;

import com.pbteach.api.cms.CmsPageControllerApi;
import com.pbteach.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by pbteach.com on 2018/6/23.
 */
@FeignClient(XcServiceList.XC_SERVICE_MANAGE_CMS)//FeignClient从eureka中找一个可用的服务
public interface CmsPageClient extends CmsPageControllerApi {
}
