package com.zwy.aicodegenerator.core.saver.impl;

import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.core.saver.CodeFileSaverTemplate;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件保存器
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    /**
     * 获取代码生成类型的方法
     * 该方法重写了父类中的getCodeType()方法，指定返回代码生成类型为HTML
     *
     * @return 返回代码生成类型枚举值，这里返回HTML类型
     */
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    /**
     * 保存HTML文件到指定目录
     *
     * @param result      包含HTML代码的结果对象
     * @param baseDirPath 目标基础目录路径
     */
    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        // 将HTML代码写入到指定路径的index.html文件中
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        if (StringUtils.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码不能为空");
        }
    }
}
