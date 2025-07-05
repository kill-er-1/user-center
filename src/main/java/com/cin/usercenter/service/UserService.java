package com.cin.usercenter.service;

import com.cin.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author cin
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-07-05 01:20:07
*/
public interface UserService extends IService<User> {
   String USER_LOGIN_STATE = "userLoginState";
   int ADMIN_ROLE = 1;

    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount , String userPassword , String checkPassword);

    /**
     * 登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    User userLogin(String userAccount , String userPassword, HttpServletRequest httpServletRequest);

    User getSafeteUser(User user);
}
