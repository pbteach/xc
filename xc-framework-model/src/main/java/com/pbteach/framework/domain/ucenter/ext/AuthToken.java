package com.pbteach.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by pbteach.com on 2018/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {
    String access_token;//访问token，就是短令牌
    String refresh_token;//刷新token
    String jwt_token;//jwt令牌
}
