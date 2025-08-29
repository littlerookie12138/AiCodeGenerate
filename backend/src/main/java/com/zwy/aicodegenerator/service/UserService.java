package com.zwy.aicodegenerator.service;

import com.mybatisflex.core.service.IService;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.vo.LoginUserVO;

/**
 *  服务层。
 *
 * @author zhangweiyan
 */
public interface UserService extends IService<User> {

    boolean register(String userAccount, String userName, String userPassword, String checkPassword);

    LoginUserVO login(String userAccount, String userPassword);
}
