package com.baiye.baiyeaiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;


/**
 * ClassName: LoveAppContextualQueryAugmenterFactory
 * Package: com.baiye.baiyeaiagent.rag
 * Description: 创建上下文查询增强器的工厂
 *
 * @Author 白夜
 * @Create 2026/1/16 20:21
 * @Version 1.0
 */
public class LoveAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                有问题可以联系客服
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }
}
