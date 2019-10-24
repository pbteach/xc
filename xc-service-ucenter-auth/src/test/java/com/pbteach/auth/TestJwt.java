package com.pbteach.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-15 10:50
 **/
public class TestJwt {

    @Test
    public void testCreateJwt(){
        //密钥库地址（基于classpath）
        String keystore_location = "xc.keystore2";
        //密钥库密码
        String keystore_password = "xuechengkeystore";

        //生成jwt令牌
        //CharSequence content 令牌内容, Signer signer 签名

        //加载密钥库
        //Resource resource, char[] password
        ClassPathResource classPathResource = new ClassPathResource(keystore_location);
        //密钥库
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());

        //取出密钥对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("xckey", "xuecheng".toCharArray());

        //取出私钥
        PrivateKey aPrivate = keyPair.getPrivate();
        //创建签名对象,采用RSA算法
        RsaSigner rsaSigner = new RsaSigner((RSAPrivateKey) aPrivate);
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "itcast");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        String toJSONString = JSON.toJSONString(tokenMap);

        Jwt encode = JwtHelper.encode(toJSONString, rsaSigner);
        //取出jwt令牌
        String encoded = encode.getEncoded();
        System.out.println(encode);
        //校验jwt令牌合法
//        JwtHelper.decodeAndVerify()

    }
    //校验方法
    @Test
    public void testVerifyJwt(){
        //jwt令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1MzE5MjMwNzQsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6ImRhNGVjYTBmLWQxMmUtNGEyMS1hYWY4LTM4M2Y5YTgzMGRmYyIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.dWJGpHgQ5pICbgvfQ95kT_D42XEVTQMUu361ESaDuHFWY2hPKmulzMrsL27vmmIkuhdmeobo1Clv13h2wR2-j2aV5xuPeje_cv90g82UtkNhVpQ92acqE4rNf6FGWjj2DkkwDcCCeesbNKaM_uHT0-SxIJjymAR0H1Fn8iFpFv92a9cLnFd1Y3jrYtZAbgB_SXnvZVmk8ikWrbm5LmJr_kE9mYUaOwiZTCVDardjARznGt2ebo98RfGEbb1PzxNEpFahMuzxhITgfO4d860BGXpCU1kEj3soWG5VZwYpHRUJ5iKc6yJK5eAaS4iy6RSb1zQfseZHuywRtOMAxsg3gw";

        //公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";

        //创建RSA签名校验对象
        RsaVerifier rsaVerifier = new RsaVerifier(publicKey);

        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);
        //校验jwt令牌
        String claims = jwt.getClaims();
        System.out.println(claims);
    }


}
