package com.zwy.aicodegenerator.utils;

import org.springframework.util.DigestUtils;

public class StringDealUtils {


    public static String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "zwy";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
