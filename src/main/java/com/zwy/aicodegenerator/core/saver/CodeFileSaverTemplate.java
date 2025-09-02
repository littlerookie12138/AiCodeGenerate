package com.zwy.aicodegenerator.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 抽象代码文件保存器
 *
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    public final File saveCode(T result, Long appId) {
        // 1.验证输入
        validateInput(result);
        // 2.构建目录
        String baseDirPath = buildUniqueDir(appId);
        // 3.保存文件（具体实现交给子类）
        saveFiles(result, baseDirPath);

        // 4.返回生成后的文件
        return new File(baseDirPath);
    }

    /**
     * 验证输入结果对象的有效性
     *
     * @param result 需要验证的结果对象
     * @throws BusinessException 当结果对象为空时抛出业务异常
     */
    protected void validateInput(T result) {
        // 检查结果对象是否为null
        if (Objects.isNull(result)) {
            // 如果结果对象为空，抛出带有系统错误代码和错误信息的业务异常
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码结果对象不能为空");
        }
    }

    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     */
    protected String buildUniqueDir(Long appId) {
        ThrowUtils.throwIf(appId == null, ErrorCode.SYSTEM_ERROR, "appId不能为空");

        String bizType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", bizType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 写入单个文件
     */
    public final void writeToFile(String dirPath, String filename, String content) {
        if (StringUtils.isNoneBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取代码类型的抽象方法
     * 这是一个抽象方法，需要在子类中实现具体的逻辑
     *
     * @return CodeGenTypeEnum 返回代码类型枚举值
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 抽象方法，用于保存文件
     *
     * @param result      包含要保存的文件数据的结果对象
     * @param baseDirPath 文件保存的基础目录路径
     *                    <p>
     *                    注意：这是一个抽象方法，需要由子类具体实现文件保存的逻辑
     */
    protected abstract void saveFiles(T result, String baseDirPath);
}
