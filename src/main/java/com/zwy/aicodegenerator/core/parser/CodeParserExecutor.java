package com.zwy.aicodegenerator.core.parser;

import com.zwy.aicodegenerator.core.parser.impl.HtmlCodeParser;
import com.zwy.aicodegenerator.core.parser.impl.MultipleFileCodeParser;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;

/**
 * 代码解析器执行器
 */
public class CodeParserExecutor {

    public static final HtmlCodeParser HTML_CODE_PARSER = new HtmlCodeParser();
    public static final MultipleFileCodeParser MULTIPLE_FILE_CODE_PARSER = new MultipleFileCodeParser();

    /**
     * 执行代码解析器，根据指定的代码类型枚举将代码内容解析为相应的对象
     *
     * @param codeContent 需要解析的代码内容字符串
     * @param typeEnum    代码类型枚举，指定要解析的代码类型
     * @return 解析后的对象，具体类型取决于传入的typeEnum
     * @throws BusinessException 当传入不支持的代码类型时抛出业务异常
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum typeEnum) {
        // 使用switch表达式根据不同的代码类型枚举值执行相应的解析逻辑
        return switch (typeEnum) {
            // 当类型为HTML时，使用HTML代码解析器解析代码内容
            case HTML -> HTML_CODE_PARSER.parseCode(codeContent);
            // 当类型为MULTI_FILE时，使用多文件代码解析器解析代码内容
            case MULTI_FILE -> MULTIPLE_FILE_CODE_PARSER.parseCode(codeContent);
            // 对于其他未支持的类型，抛出系统业务异常
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "暂不支持的代码解析类型");
        };
    }
}
