package com.zwy.aicodegenerator.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.mapper.UserMapper;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.model.enums.UserRoleEnum;
import com.zwy.aicodegenerator.model.vo.LoginUserVO;
import com.zwy.aicodegenerator.service.UserService;
import com.zwy.aicodegenerator.utils.StringDealUtils;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 服务层实现。
 *
 * @author zhangweiyan
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean register(String userAccount, String userName, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = StringDealUtils.getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userName);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return Objects.nonNull(user.getId());
    }

    /**
     * 登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public LoginUserVO login(String userAccount, String userPassword) {
        // 1.校验账号不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(userAccount), ErrorCode.PARAMS_ERROR, "账号不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(userPassword), ErrorCode.PARAMS_ERROR, "密码不能为空");

        String encryptPassword = StringDealUtils.getEncryptPassword(userPassword);

        // 查询用户
        QueryWrapper qw = new QueryWrapper();
        qw.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        List<User> users = mapper.selectListByQuery(qw);
        ThrowUtils.throwIf(CollUtil.isEmpty(users), ErrorCode.PARAMS_ERROR, "账号或密码错误");
        ThrowUtils.throwIf(users.size() > 1, ErrorCode.SYSTEM_ERROR, "账号密码非唯一");


        return null;
    }


}
