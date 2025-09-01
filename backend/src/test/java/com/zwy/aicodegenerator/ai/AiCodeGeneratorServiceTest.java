package com.zwy.aicodegenerator.ai;

import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiCodeGeneratorServiceTest {
    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHTMLCode() {
        HtmlCodeResult res = aiCodeGeneratorService.generateHTMLCode("做个程序员熊岩的博客，不超过20行");
        Assertions.assertNotNull(res);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult res = aiCodeGeneratorService.generateMultiFileCode("做个程序员熊岩的留言板，不超过50行");
        Assertions.assertNotNull(res);
    }
}