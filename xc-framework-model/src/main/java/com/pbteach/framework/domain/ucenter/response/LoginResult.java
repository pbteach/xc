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
public class LoginResult extends ResponseResult {
    public LoginResult(ResultCode resultCode,String token) {
        super(resultCode);
        this.token = token;
    }
    private String token;
}
