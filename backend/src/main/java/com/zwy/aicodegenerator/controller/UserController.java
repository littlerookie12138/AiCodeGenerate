package com.zwy.aicodegenerator.controller;


import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.zwy.aicodegenerator.annotation.AuthCheckAnnotation;
import com.zwy.aicodegenerator.common.BaseResponse;
import com.zwy.aicodegenerator.constants.UserConstant;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.dto.user.*;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.model.vo.LoginUserVO;
import com.zwy.aicodegenerator.model.vo.UserVO;
import com.zwy.aicodegenerator.service.UserService;
import com.zwy.aicodegenerator.utils.ResultUtils;
import com.zwy.aicodegenerator.utils.StringDealUtils;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 控制层。
 *
 * @author zhangweiyan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public BaseResponse<Boolean> register(@RequestBody UserRegisterRequest user) {
        ThrowUtils.throwIf(Objects.isNull(user), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.register(user.getUserAccount(), user.getUserName(), user.getUserPassword(), user.getCheckPassword()));
    }

    /**
     * 登录用户
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest user, HttpServletRequest request) {
        ThrowUtils.throwIf(Objects.isNull(user), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.login(user.getUserAccount(), user.getUserPassword(), request));
    }

    /**
     * 用户注销
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }

    /**
     * 创建用户
     */
    @PostMapping("/add")
    @AuthCheckAnnotation(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = StringDealUtils.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        // 查找已被逻辑删除的用户
        boolean result = false;
        User exist = userService.findDeletedById(user.getUserAccount());
        if (Objects.nonNull(exist)) {
            user.setIsDelete(0);
            user.setUserPassword(encryptPassword);
            BeanUtil.copyProperties(exist, user, false);
            userService.updateById(exist);
        } else {
            result = userService.save(user);
        }
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheckAnnotation(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVo(user));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @AuthCheckAnnotation(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody UserUpdateRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/me")
    public BaseResponse<User> getLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getLoginUser(request));
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @AuthCheckAnnotation(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @AuthCheckAnnotation(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVoList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

}
