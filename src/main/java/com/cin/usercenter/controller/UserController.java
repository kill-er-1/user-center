package com.cin.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cin.usercenter.mapper.UserMapper;
import com.cin.usercenter.model.User;
import com.cin.usercenter.model.domin.request.UserLoginRequest;
import com.cin.usercenter.model.domin.request.UserRegisterRequest;
import com.cin.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cin.usercenter.constant.userconstant.ADMIN_ROLE;
import static com.cin.usercenter.constant.userconstant.USER_LOGIN_STATE;

/**
 * @author cin
 * @version 1.0
 * @date 2025/7/5 10:03
 * @className UserController
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping ("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest , HttpServletRequest httpServletRequest){
        if( userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, httpServletRequest);
    }

    /**
     * 查询用户
     * @param username
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/search")
    public List<User> searchUser(String username,HttpServletRequest httpServletRequest){
        if (!isAdmin(httpServletRequest)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username) ;
        }

        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafeteUser(user)).collect(Collectors. toList ());

    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest httpServletRequest){
        if (!isAdmin(httpServletRequest)) {
            return false;
        }
        if(id < 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断是否为管理员
     * @param httpServletRequest
     * @return
     */
    private boolean isAdmin(HttpServletRequest httpServletRequest){
        Object obj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user  = (User)obj;
        if(user == null || user.getRole() != ADMIN_ROLE  ){
            return false;
        }
        return true;
    }
}
