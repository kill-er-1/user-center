package com.cin.usercenter.model.domin.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cin
 * @version 1.0
 * @date 2025/7/5 10:28
 * @className UserLoginRequest
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serializableUid = 123;
    private String userAccount;
    private String userPassword;
}
