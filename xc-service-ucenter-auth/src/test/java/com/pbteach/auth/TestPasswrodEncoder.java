package com.pbteach.auth;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-17 9:51
 **/
public class TestPasswrodEncoder {

    @Test
    public void testBCryptPasswordEncoder(){

        for(int i=0;i<10;i++){
            PasswordEncoder passwordEncoder = new  BCryptPasswordEncoder();

            String encode = passwordEncoder.encode("111111");
            System.out.println(encode);

            //校验Hash值是否和原始密码一致
            //CharSequence rawPassword, String encodedPassword
            boolean matches = passwordEncoder.matches("111111", encode);
            System.out.println(matches);
        }

    }
}
