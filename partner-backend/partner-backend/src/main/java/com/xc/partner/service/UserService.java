package com.xc.partner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.partner.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author Volder
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-09-28 14:21:02
*/
public interface UserService extends IService<User> {
    /**
     * 用户注释
     * @param userAccount 账户
     * @param password  密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount,String password,String checkPassword,String planetCode);

    User userLongin(String userAccount, String password, HttpServletRequest request);

    User getSafetyUser(User user);

    List<User> searchUsers(String username);

    boolean removeById(long id);

    int userLogout(HttpServletRequest request);
}
