package com.zwy.aicodegenerator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.mapper.AppMapper;
import com.zwy.aicodegenerator.model.dto.app.AppQueryRequest;
import com.zwy.aicodegenerator.model.entity.App;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.model.vo.AppVO;
import com.zwy.aicodegenerator.model.vo.UserVO;
import com.zwy.aicodegenerator.service.AppService;
import com.zwy.aicodegenerator.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author zhangweiyan
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {
    @Resource
    private UserService userService;

    @Override
    public AppVO getAppVO(App app) {
        // 参数校验：如果传入的app对象为null，则直接返回null
        if (app == null) {
            return null;
        }
        // 创建AppVO对象，并使用BeanUtil将app对象的属性复制到appVO中
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        // 判断userId是否为null，不为null则查询用户信息
        if (userId != null) {
            // 根据userId查询用户信息
            User user = userService.getById(userId);
            // 将User对象转换为UserVO对象
            UserVO userVO = userService.getUserVo(user);
            // 将userVO设置到appVO中
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        // 检查传入的appList是否为空，若为空则返回空ArrayList
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息，避免 N+1 查询问题
        // 从appList中提取所有用户ID，并转换为Set集合
        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        // 根据用户ID批量查询用户信息，并将结果转换为Map，以用户ID为键
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVo));
        // 遍历appList，将每个App对象转换为AppVO对象，并设置对应的用户信息
        return appList.stream().map(app -> {
            // 将App对象转换为AppVO对象
            AppVO appVO = getAppVO(app);
            // 从userVOMap中获取对应的UserVO对象
            UserVO userVO = userVOMap.get(app.getUserId());
            // 将UserVO对象设置到AppVO中
            appVO.setUser(userVO);
            // 返回处理后的AppVO对象
            return appVO;
        }).collect(Collectors.toList()); // 将处理后的AppVO对象收集为List并返回
    }


    @Override
    /**
     * 根据应用查询请求参数构建查询条件包装器
     * @param appQueryRequest 应用查询请求参数对象，包含查询条件
     * @return QueryWrapper 构建好的查询条件包装器，用于数据库查询
     * @throws BusinessException 当请求参数为空时抛出业务异常
     */
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        // 检查请求参数是否为空，为空则抛出业务异常
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 从请求参数中获取各个查询条件
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getOrderBy();
        String sortOrder = appQueryRequest.getOrderType();
        // 构建查询条件包装器，添加各个查询条件
        return QueryWrapper.create()
                // 精确匹配id
                .eq("id", id)
                // 模糊匹配应用名称
                .like("appName", appName)
                // 模糊匹配封面
                .like("cover", cover)
                // 模糊匹配初始化提示
                .like("initPrompt", initPrompt)
                // 精确匹配代码生成类型
                .eq("codeGenType", codeGenType)
                // 精确匹配部署密钥
                .eq("deployKey", deployKey)
                // 精确匹配优先级
                .eq("priority", priority)
                // 精确匹配用户ID
                .eq("userId", userId)
                // 按指定字段排序，"ascend"表示升序
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


}
