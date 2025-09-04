package com.zwy.aicodegenerator.generatorRes.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zwy.aicodegenerator.generatorRes.entity.ChatHistory;
import com.zwy.aicodegenerator.generatorRes.mapper.ChatHistoryMapper;
import com.zwy.aicodegenerator.generatorRes.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author zhangweiyan
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
