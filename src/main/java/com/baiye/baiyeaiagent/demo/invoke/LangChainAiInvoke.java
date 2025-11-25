package com.baiye.baiyeaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;


/**
 * LangChain4j 调用AI大模型
 */
public class LangChainAiInvoke {

    public static void main(String[] args) {
        QwenChatModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat("我是白夜");
        System.out.println(answer);
    }
}
