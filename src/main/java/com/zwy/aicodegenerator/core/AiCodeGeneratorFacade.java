package com.zwy.aicodegenerator.core;

import com.zwy.aicodegenerator.ai.AiCodeGeneratorService;
import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.ai.model.MultiFileCodeResult;
import com.zwy.aicodegenerator.core.parser.CodeParserExecutor;
import com.zwy.aicodegenerator.core.saver.CodeFileSaverExecutor;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.Objects;

/**
 * AI代码生成门面类， 组合代码生成和保存功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口 根据类型生成并保存代码
     *
     * @param userMsg
     * @param codeGenType
     * @param appId
     * @return
     */
    public File generateAndSaveCode(String userMsg, CodeGenTypeEnum codeGenType, Long appId) {
        ThrowUtils.throwIf(Objects.isNull(codeGenType), ErrorCode.PARAMS_ERROR, "生成类型不能为空");

        return switch (codeGenType) {
            case HTML -> {

                // 调用AI代码生成服务生成多文件代码结果
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHTMLCode(userMsg);

                // 保存单文件代码结果并返回文件对象
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {

                // 调用AI代码生成服务生成HTML代码，传入用户消息作为参数
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMsg);

                // 使用代码保存工具将生成的多文件代码结果保存为文件，并返回该文件对象
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default ->
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的生成类型" + codeGenType.getValue());
        };
    }

    /**
     * 统一入口 根据类型生成流式代码
     *
     * @param userMsg
     * @param codeGenType
     * @param appId
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMsg, CodeGenTypeEnum codeGenType, Long appId) {
        ThrowUtils.throwIf(Objects.isNull(codeGenType), ErrorCode.PARAMS_ERROR, "生成类型不能为空");

        return switch (codeGenType) {
            case HTML -> {
                Flux<String> result = aiCodeGeneratorService.generateHTMLCodeStream(userMsg);
                yield processCodeStream(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMsg);
                yield processCodeStream(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default ->
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的生成类型" + codeGenType.getValue());
        };
    }


    /**
     * 处理代码流的方法
     *
     * @param codeStream  代码流，包含多个代码片段
     * @param codeGenType 代码生成类型
     * @param appId       应用ID
     * @return 返回处理后的代码流
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        // 定义一个字符串拼接器 用于流式返回所有代码后再保存代码
        StringBuilder sb = new StringBuilder();

        return codeStream.doOnNext(sb::append).doOnComplete(() -> {
            try {
                // 完成后保存代码
                String totalCode = sb.toString();
                // 执行代码解析器，解析完整代码
                // 执行代码保存器，将解析后的代码保存到文件
                Object afterParsedRes = CodeParserExecutor.executeParser(totalCode, codeGenType);
                // 记录文件创建成功的日志
                File file = CodeFileSaverExecutor.executeSaver(afterParsedRes, codeGenType, appId);
                log.info("文件创建完成， 目录为{}", file.getAbsolutePath());
            } catch (Exception e) {
                // 记录文件创建失败的日志
                e.printStackTrace();
                log.error("文件创建失败, {}", e.getMessage());
            }
        });
    }
}
