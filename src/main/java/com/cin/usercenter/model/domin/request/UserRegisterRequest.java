package com.cin.usercenter.model.domin.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cin
 * @version 1.0
 * @date 2025/7/5 10:14
 * @className UserControllerRequest
 */

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
