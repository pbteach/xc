package com.pbteach.framework.domain.order.request;

import com.pbteach.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/3/26.
 */
@Data
@ToString
public class CreateOrderRequest extends RequestData {

    String courseId;

}
