package com.cin.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cin.usercenter.model.User;
import com.cin.usercenter.service.UserService;
import com.cin.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author cin
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-07-05 01:20:07
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;

    private static final String SALT = "cin";



    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验账号密码，非空，位数，账号里是否有特殊字符，密码与验证密码一致性
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }

        if(userAccount.length()<4){
            return -1;
        }
        if(userPassword.length()<8||checkPassword.length()<8){
            return -1;
        }
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        //非法账户
        String invalidateRegExp = "[\\pP\\pS\\s]";
        Matcher matcher = Pattern.compile(invalidateRegExp).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }
        //2.查询账户是否存在
        //设置查询条件，执行查询,mapper执行
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if(count >= 1){
            return -1;
        }
        //3.对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //4.插入
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }

        return user.getId();
    }

    /**
     * 用户登陆
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //1.校验账号密码，非空，位数，账号里是否有特殊字符
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }

        if(userAccount.length()<4){
            return null;
        }
        if(userPassword.length()<8){
            return null;
        }
        //2非法
        String invalidateRegExp = "[\\pP\\pS\\s]";
        Matcher matcher = Pattern.compile(invalidateRegExp).matcher(userAccount);
        if(matcher.find()){
            return null;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3验证密码正确性
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("the user isn't in it");
            return null;
        }
        //4.用户脱敏
        User safetyUser = getSafeteUser(user);

        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafeteUser(User user) {
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUpdateTime(user.getUpdateTime());
        safetyUser.setIsDelete(user.getIsDelete());
        safetyUser.setRole(user.getRole());
        return safetyUser;
    }

    /**
     * 是否为管理员
     * @param httpServletRequest
     * @return
     */
    private boolean isAdmin(HttpServletRequest httpServletRequest){
        Object obj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user  = (User)obj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }
}




