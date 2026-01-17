package com.baiye.baiyeaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: MyKeywordEnricher
 * Package: com.baiye.baiyeaiagent.rag
 * Description: 基于 AI 的文档元信息增强器（为文档补充元信息）
 *
 * @Author 白夜
 * @Create 2026/1/16 17:39
 * @Version 1.0
 */
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        return  keywordMetadataEnricher.apply(documents);
    }
}
