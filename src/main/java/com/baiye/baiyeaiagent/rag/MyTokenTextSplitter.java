package com.baiye.baiyeaiagent.rag;


import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: MyTokenTextSplitter
 * Package: com.baiye.baiyeaiagent.rag
 * Description: 自定义基于 Token 的切词器
 *
 * @Author 白夜
 * @Create 2026/1/16 20:24
 * @Version 1.0
 */
@Component
class MyTokenTextSplitter {
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }

    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(200, 100, 10, 5000, true);
        return splitter.apply(documents);
    }
}
