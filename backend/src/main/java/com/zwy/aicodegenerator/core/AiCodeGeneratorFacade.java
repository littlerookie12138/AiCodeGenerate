package com.zwy.aicodegenerator.core;

import com.zwy.aicodegenerator.ai.AiCodeGeneratorService;
import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.ai.model.MultiFileCodeResult;
import com.zwy.aicodegenerator.exception.BusinessException;
import com.zwy.aicodegenerator.model.enums.CodeGenTypeEnum;
import com.zwy.aicodegenerator.model.enums.ErrorCode;
import com.zwy.aicodegenerator.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
     * @return
     */
    public File generateAndSaveCode(String userMsg, CodeGenTypeEnum codeGenType) {
        ThrowUtils.throwIf(Objects.isNull(codeGenType), ErrorCode.PARAMS_ERROR, "生成类型不能为空");

        return switch (codeGenType) {
            case HTML -> generateHtmlCodeAndSave(userMsg);
            case MULTI_FILE -> generateMultiFileCodeAndSave(userMsg);
            default ->
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的生成类型" + codeGenType.getValue());
        };
    }

    /**
     * 统一入口 根据类型生成流式代码
     *
     * @param userMsg
     * @param codeGenType
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMsg, CodeGenTypeEnum codeGenType) {
        ThrowUtils.throwIf(Objects.isNull(codeGenType), ErrorCode.PARAMS_ERROR, "生成类型不能为空");

        return switch (codeGenType) {
            case HTML -> generateHtmlCodeAndSaveStream(userMsg);
            case MULTI_FILE -> generateMultiFileCodeAndSaveStream(userMsg);
            default ->
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的生成类型" + codeGenType.getValue());
        };
    }

    /**
     * 生成多文件代码并保存为流的方法
     * 该方法接收用户消息，返回一个包含字符串的Flux流
     *
     * @param userMsg 用户输入的消息，用于生成代码
     * @return Flux<String> 返回一个字符串类型的Flux流，用于处理生成的多文件代码
     */
    private Flux<String> generateMultiFileCodeAndSaveStream(String userMsg) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMsg);
        // 定义一个字符串拼接器 用于流式返回所有代码后再保存代码
        StringBuilder sb = new StringBuilder();

        return result.doOnNext(sb::append).doOnComplete(() -> {
            try {
                // 完成后保存代码
                String totalCode = sb.toString();
                MultiFileCodeResult res = CodeParser.parseMultiFileCode(totalCode);
                File file = CodeFileSaver.saveMultiFileCodeResult(res);
                log.info("文件创建完成， 目录为{}", file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("文件创建失败, {}", e.getMessage());
            }
        });
    }

    /**
     * 生成HTML代码并保存为流的方法
     *
     * @param userMsg 用户输入的消息
     * @return 返回一个包含HTML代码的Flux流
     */
    private Flux<String> generateHtmlCodeAndSaveStream(String userMsg) {
        Flux<String> result = aiCodeGeneratorService.generateHTMLCodeStream(userMsg);
        // 定义一个字符串拼接器 用于流式返回所有代码后再保存代码
        StringBuilder sb = new StringBuilder();

        return result.doOnNext(sb::append).doOnComplete(() -> {
            try {
                // 完成后保存代码
                String totalCode = sb.toString();
                HtmlCodeResult res = CodeParser.parseHtmlCode(totalCode);
                File file = CodeFileSaver.saveHtmlCodeResult(res);
                log.info("文件创建完成， 目录为{}", file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("文件创建失败, {}", e.getMessage());

            }
        });
    }

    /**
     * 生成多文件代码并保存到本地
     * 该方法调用AI代码生成服务生成HTML代码，然后使用代码保存工具将结果保存为文件
     *
     * @param userMsg 用户输入的消息，将作为生成HTML代码的输入参数
     * @return File 返回生成的HTML文件对象
     */
    private File generateMultiFileCodeAndSave(String userMsg) {
        // 调用AI代码生成服务生成HTML代码，传入用户消息作为参数
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMsg);

        // 使用代码保存工具将生成的HTML代码结果保存为文件，并返回该文件对象
        return CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
    }

    /**
     * 生成HTML代码并保存为文件
     *
     * @param userMsg 用户输入的消息，用于生成相应的代码
     * @return 生成的代码文件对象
     */
    private File generateHtmlCodeAndSave(String userMsg) {
        // 调用AI代码生成服务生成多文件代码结果
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHTMLCode(userMsg);

        // 保存多文件代码结果并返回文件对象
        return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
    }
}
