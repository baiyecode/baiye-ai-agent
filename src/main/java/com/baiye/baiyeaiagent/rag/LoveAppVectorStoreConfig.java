package com.baiye.baiyeaiagent.rag;

import jakarta.annotation.Resource;

import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ClassName: LoveAppVectorStoreConfig
 * Package: com.baiye.baiyeaiagent.rag
 * Description: 恋爱大师向量数据库配置（初始化基于内存的向量数据库bean）
 *
 * @Author 白夜
 * @Create 2025/11/29 15:47
 * @Version 1.0
 */
@Configuration
public class LoveAppVectorStoreConfig {

    //https://docs.spring.io/spring-ai/reference/api/vectordbs.html#_vectorstore_implementations

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;


    //在将文档写入到数据库前，会先调用 Embedding 大模型将文档转换为向量，实际保存到数据库中的是向量类型的数据。
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        // 自主切分文档
//        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documentList);
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }
}

