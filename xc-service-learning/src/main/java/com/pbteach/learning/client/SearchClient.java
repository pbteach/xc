package com.pbteach.learning.client;

import com.pbteach.api.search.EsCourseControllerApi;
import com.pbteach.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by pbteach.com on 2018/7/14.
 */
@FeignClient(value= XcServiceList.XC_SERVICE_SEARCH)
public interface SearchClient extends EsCourseControllerApi {

}
