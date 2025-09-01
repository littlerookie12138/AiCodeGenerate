package com.zwy.aicodegenerator.core;

import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {
    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个登录页面, 总共不超过50行代码", CodeGenTypeEnum.MULTI_FILE);
    }


    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个登录页面, 总共不超过50行代码", CodeGenTypeEnum.HTML);
        // 阻塞等待所有数据
        List<String> resList = codeStream.collectList().block();
        // 拼接完整内容
        String totalResStr = String.join("", resList);
    }
}