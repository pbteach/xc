package com.pbteach.auth.client;

import com.pbteach.api.ucenter.UcenterControllerApi;
import com.pbteach.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by pbteach.com on 2018/7/17.
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient extends UcenterControllerApi {
}
