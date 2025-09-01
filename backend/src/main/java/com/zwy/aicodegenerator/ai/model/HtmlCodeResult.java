package com.zwy.aicodegenerator.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HtmlCodeResult 类用于存储HTML、CSS、JS代码及其描述信息
 * 使用@Data注解自动生成getter、setter、toString等方法
 */
@Data
public class HtmlCodeResult {

    @Description("存储HTML代码的字符串")
    private String htmlCode;    // 存储HTML代码的字符串

    @Description("存储代码描述信息的字符串")
    private String description; // 存储代码描述信息的字符串
}
