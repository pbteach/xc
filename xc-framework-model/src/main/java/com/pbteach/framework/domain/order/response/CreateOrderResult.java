package com.pbteach.framework.domain.order.response;

import com.pbteach.framework.domain.order.XcOrders;
import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/3/26.
 */
@Data
@ToString
public class CreateOrderResult extends ResponseResult {
    private XcOrders xcOrders;
    public CreateOrderResult(ResultCode resultCode, XcOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }


}
