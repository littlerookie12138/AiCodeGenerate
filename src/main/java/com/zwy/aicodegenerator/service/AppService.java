package com.zwy.aicodegenerator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zwy.aicodegenerator.model.dto.app.AppQueryRequest;
import com.zwy.aicodegenerator.model.entity.App;
import com.zwy.aicodegenerator.model.entity.User;
import com.zwy.aicodegenerator.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 服务层。
 *
 * @author zhangweiyan
 */
public interface AppService extends IService<App> {

    /**
     * 根据App实体对象获取对应的AppVO（View Object）对象
     *
     * @param app App实体对象，包含应用的详细信息
     * @return AppVO 视图对象，通常用于前端展示，可能只包含部分必要信息或经过格式化处理
     */
    AppVO getAppVO(App app);

    /**
     * 根据应用查询请求参数构建查询条件包装器
     *
     * @param appQueryRequest 应用查询请求参数对象，包含查询条件
     * @return QueryWrapper 返回构建好的查询条件包装器，用于数据库查询
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取应用视图对象列表(AppVO)
     * 该方法将应用实体列表(App)转换为视图对象列表(AppVO)，通常用于数据展示层
     *
     * @param appList 应用实体列表，包含应用的基本信息
     * @return 返回转换后的应用视图对象列表，用于前端展示或数据传输
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 根据应用ID、用户消息和当前登录用户生成代码的响应式流
     *
     * @param appId     应用程序的唯一标识符
     * @param msg       用户输入的消息内容
     * @param loginUser 当前登录的用户信息
     * @return 返回一个包含生成代码的字符串的Flux流，用于异步处理多个代码片段或流式输出
     */
    Flux<String> chatToGenCode(Long appId, String msg, User loginUser);

}
