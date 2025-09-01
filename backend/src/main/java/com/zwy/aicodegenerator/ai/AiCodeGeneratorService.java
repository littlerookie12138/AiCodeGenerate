package com.zwy.aicodegenerator.ai;

import com.zwy.aicodegenerator.ai.model.HtmlCodeResult;
import com.zwy.aicodegenerator.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

public interface AiCodeGeneratorService {

    /**
     * 生成HTML代码
     * @param userMsg  用户提示词
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHTMLCode(String userMsg);

    /**
     * 生成多文件代码
     * @param userMsg  用户提示词
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMsg);


    /**
     * 生成HTML代码 流式输出
     * @param userMsg  用户提示词
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHTMLCodeStream(String userMsg);

    /**
     * 生成多文件代码 流式输出
     * @param userMsg  用户提示词
     * @return
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMsg);
}
