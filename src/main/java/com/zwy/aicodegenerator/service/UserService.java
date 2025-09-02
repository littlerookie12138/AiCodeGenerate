package com.zwy.aicodegenerator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zwy.aicodegenerator.model.dto.user.UserQueryRequest;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.vo.LoginUserVO;
import com.zwy.aicodegenerator.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 服务层。
 *
 * @author zhangweiyan
 */
public interface UserService extends IService<User> {

    /**
     * 注册用户
     *
     * @param userAccount
     * @param userName
     * @param userPassword
     * @param checkPassword
     * @return
     */
    boolean register(String userAccount, String userName, String userPassword, String checkPassword);

    /**
     * 登录用户
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取缓存的用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);


    /**
     * 根据User实体获取User视图对象
     *
     * @param user 用户实体对象
     * @return UserVO 用户视图对象，包含展示给前端的数据
     */
    UserVO getUserVo(User user);

    /**
     * 根据用户列表获取用户视图对象列表
     *
     * @param userList 用户实体列表，包含完整的用户信息
     * @return 用户视图对象列表，通常用于前端展示，包含用户的基本信息
     */
    List<UserVO> getUserVoList(List<User> userList);

    /**
     * 根据用户查询请求构建QueryWrapper对象
     *
     * @param userQueryRequest 用户查询请求对象，包含查询条件
     * @return 返回一个QueryWrapper对象，用于构建数据库查询条件
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    User findDeletedById(String userAccount);
}
