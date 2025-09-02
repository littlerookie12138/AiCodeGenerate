package com.zwy.aicodegenerator.core.saver;

import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.ai.model.MultiFileCodeResult;
import com.zwy.aicodegenerator.core.parser.impl.HtmlCodeParser;
import com.zwy.aicodegenerator.core.parser.impl.MultipleFileCodeParser;
import com.zwy.aicodegenerator.core.saver.impl.HtmlCodeFileSaverTemplate;
import com.zwy.aicodegenerator.core.saver.impl.MultiFileCodeFileSaverTemplate;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;

import java.io.File;

/**
 * 代码保存执行器
 */
public class CodeFileSaverExecutor {

    public static final HtmlCodeFileSaverTemplate HTML_CODE_SAVER = new HtmlCodeFileSaverTemplate();
    public static final MultiFileCodeFileSaverTemplate MULTIPLE_FILE_CODE_SAVER = new MultiFileCodeFileSaverTemplate();

    public static File executeSaver(Object result, CodeGenTypeEnum typeEnum, Long appId) {
        // 使用switch表达式根据不同的代码类型枚举值执行相应的解析逻辑
        return switch (typeEnum) {
            // 当类型为HTML时，使用HTML代码解析器解析代码内容
            case HTML -> HTML_CODE_SAVER.saveCode((HtmlCodeResult) result, appId);
            // 当类型为MULTI_FILE时，使用多文件代码解析器解析代码内容
            case MULTI_FILE -> MULTIPLE_FILE_CODE_SAVER.saveCode((MultiFileCodeResult) result, appId);
            // 对于其他未支持的类型，抛出系统业务异常
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "暂不支持的代码保存类型");
        };
    }
}
