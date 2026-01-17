package com.baiye.baiyeaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: LoveAppDocumentLoader
 * Package: com.baiye.baiyeaiagent.rag
 * Description:  Spring AI ETL（提取、转换、加载）,处理 Markdown 文档，将其转换为文档对象列表。
 *
 * @Author 白夜
 * @Create 2025/11/28 23:06
 * @Version 1.0
 */
@Component
@Slf4j
class LoveAppDocumentLoader {

    //https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html#_markdown

    //依赖注入，解析资源位置模式（如 classpath*:document/*.md）并返回对应的 Resource 对象数组。
    private final ResourcePatternResolver resourcePatternResolver;

    LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            // 这里可以修改为你要加载的多个 Markdown 文件的路径模式
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();// 获取文件名
                // 提取文档倒数第 3 和第 2 个字作为标签
                String status = filename.substring(filename.length() - 6, filename.length() - 4);
                //配置 Markdown 解析器
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        //是否根据“水平分割线”,通常是 ---、*** 或 ___,将一个 Markdown 文件切分成多个文档对象。
                        .withHorizontalRuleCreateDocument(true)
                        //是否把代码块内容（... 或缩进代码块）包含在解析出的 Document 内容中。false 表示不包含。
                        .withIncludeCodeBlock(false)
                        //是否把引用块内容（> ...）包含在解析出的 Document 内容中。false 表示不包含。
                        .withIncludeBlockquote(false)
                        //为解析出的 Document 添加额外的元数据（Metadata）。这里添加了一个名为 filename 的键，其值为当前处理的文件名。
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("status", status)
                        .build();
                // 处理 Markdown 文档，将其转换为文档对象列表。
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文档加载失败", e);
        }
        return allDocuments;
    }
}

