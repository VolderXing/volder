package com.xc.partner.service;

import com.xc.partner.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void testSelect(){
        User user = new User();
        user.setUsername("xc");
        user.setUseraccount("testluck");
        user.setAvatarurl("D:\\桌面\\figure\\boy1.jpg");
        user.setGender(0);
        user.setUserpassword("123456");
        user.setPhone("18636578183");
        user.setEmail("2232059602@QQ.COM");


        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assert.assertTrue(result);
    }


    @Test
    void testRegister(){
        String userAccount = "long";
        String password = "12345678";
        String checkPassword = "12345678";
    }

    @Test
    void testLogin(){
        userService.userLongin("long","12345678",null);
    }
}
