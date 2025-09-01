package com.zwy.aicodegenerator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.mapper.UserMapper;
import com.zwy.aicodegenerator.model.dto.user.UserQueryRequest;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.model.enums.UserRoleEnum;
import com.zwy.aicodegenerator.model.vo.LoginUserVO;
import com.zwy.aicodegenerator.model.vo.UserVO;
import com.zwy.aicodegenerator.service.UserService;
import com.zwy.aicodegenerator.utils.StringDealUtils;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.zwy.aicodegenerator.constants.UserConstant.USER_SESSION_KEY;

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
        // 4. 插入数据 先判断是否该账号被逻辑删除 如果是，将其删除
        User exist = this.findDeletedById(userAccount);
        if (Objects.nonNull(exist)) {
            LogicDeleteManager.execWithoutLogicDelete(() -> mapper.deleteById(exist.getId()));
        }
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
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验账号不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(userAccount), ErrorCode.PARAMS_ERROR, "账号不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(userPassword), ErrorCode.PARAMS_ERROR, "密码不能为空");

        String encryptPassword = StringDealUtils.getEncryptPassword(userPassword);

        // 查询用户
        QueryWrapper qw = new QueryWrapper();
        qw.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        List<User> users = mapper.selectListByQuery(qw);
        ThrowUtils.throwIf(CollUtil.isEmpty(users), ErrorCode.PARAMS_ERROR, "账号不存在");
        ThrowUtils.throwIf(users.size() > 1, ErrorCode.SYSTEM_ERROR, "账号密码非唯一");

        // 获取到的唯一的用户
        LoginUserVO res = new LoginUserVO();
        BeanUtil.copyProperties(users.getFirst(), res);

        // 设置session
        request.getSession().setAttribute(USER_SESSION_KEY, users.getFirst());

        return res;
    }

    /**
     * 获取当前系统登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_SESSION_KEY);
        User result = null;
        if (Objects.nonNull(userObj)) {
            User currentUser = (User) userObj;
            if (Objects.nonNull(currentUser.getId())) {
                result = mapper.selectOneById(currentUser.getId());
            }
        }
        return result;
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_SESSION_KEY);
        ThrowUtils.throwIf(Objects.isNull(userObj), ErrorCode.NOT_LOGIN_ERROR, "用户未登录");

        // 移除session
        request.getSession().removeAttribute(USER_SESSION_KEY);
        return true;
    }

    @Override
    public UserVO getUserVo(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO, false);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVoList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVo).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getOrderBy();
        String sortOrder = userQueryRequest.getOrderType();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, !"desc".equals(sortOrder));
    }

    @Override
    public User findDeletedById(String userAccount) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("userAccount", userAccount)
                .eq("isDelete", 1);
        AtomicReference<User> res = new AtomicReference<>();
        LogicDeleteManager.execWithoutLogicDelete(() -> {
            res.set(mapper.selectListByQuery(qw).stream().findFirst().orElse(null));
        });
        return res.get();
    }
}
