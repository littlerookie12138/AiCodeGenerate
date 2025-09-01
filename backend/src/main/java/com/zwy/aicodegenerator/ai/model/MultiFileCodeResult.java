package com.zwy.aicodegenerator.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * MultiFileCodeResult 类用于存储HTML、CSS、JS代码及其描述信息
 * 使用@Data注解自动生成getter、setter、toString等方法
 */
@Data
public class MultiFileCodeResult {

    @Description("存储HTML代码的字符串")
    private String htmlCode;    // 存储HTML代码的字符串

    @Description("存储CSS代码的字符串")
    private String cssCode;     // 存储CSS代码的字符串

    @Description("存储JavaScript代码的字符串")
    private String jsCode;      // 存储JavaScript代码的字符串

    @Description("存储代码描述信息的字符串")
    private String description; // 存储代码描述信息的字符串
}
