package com.zwy.aicodegenerator.core.parser;

/**
 * 代码解析器
 * @param <T>
 */
public interface CodeParser<T> {

    /**
     * 解析代码内容
     * @param codeContent
     * @return 解析后的结果对象
     */
    T parseCode(String codeContent);
}
