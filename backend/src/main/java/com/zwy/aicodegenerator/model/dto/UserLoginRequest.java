package com.zwy.aicodegenerator.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端注册用户请求实体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

}
