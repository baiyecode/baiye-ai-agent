package com.baiye.baiyeaiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: LoveAppRagCloudAdvisorConfig
 * Package: com.baiye.baiyeaiagent.rag
 * Description: 自定义基于阿里云 云知识库服务的 RAG 增强顾问
 *
 * @Author 白夜
 * @Create 2025/11/29 18:09
 * @Version 1.0
 */
@Configuration
@Slf4j
class LoveAppRagCloudAdvisorConfig {

    //https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html#_retrievalaugmentationadvisor

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        //DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        //创建 DashScope API 客户端 (使用 Builder):
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(dashScopeApiKey) // 设置 API Key
                // 其他参数使用 Builder 中的默认值
                .build();
        final String KNOWLEDGE_INDEX = "恋爱大师";//知识库索引的名称
        //创建文档检索器 (DocumentRetriever)
        DocumentRetriever documentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder() //配置检索器选项
                        .withIndexName(KNOWLEDGE_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder() //创建检索增强建议器 (RetrievalAugmentationAdvisor)
                .documentRetriever(documentRetriever)
                .build();
    }
}

