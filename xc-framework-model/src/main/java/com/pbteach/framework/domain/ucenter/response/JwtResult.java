package com.pbteach.framework.domain.ucenter.response;

import com.pbteach.framework.model.response.ResponseResult;
import com.pbteach.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class JwtResult extends ResponseResult {
    public JwtResult(ResultCode resultCode, String jwt) {
        super(resultCode);
        this.jwt = jwt;
    }
    private String jwt;
}
