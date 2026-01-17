package com.baiye.baiyeaiagent.demo.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: MultiQueryExpanderDemo
 * Package: com.baiye.baiyeaiagent.demo.rag
 * Description: 查询扩展器 Demo
 *
 * @Author 白夜
 * @Create 2026/1/17 16:32
 * @Version 1.0
 */
@Component
public class MultiQueryExpanderDemo {

    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }


/**
 * 扩展查询方法，将单个查询扩展为多个相关查询
 * @param query 原始查询字符串
 * @return 扩展后的查询列表
 */
    public List<Query> expand(String query) {
        // 创建多查询扩展器构建器
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                // 设置聊天客户端构建器
                .chatClientBuilder(chatClientBuilder)
                // 设置要生成的查询数量
                .numberOfQueries(3)
                // 构建完成的多查询扩展器实例
                .build();
        // 使用扩展器处理原始查询，返回扩展后的查询列表
        List<Query> queries = queryExpander.expand(new Query("谁是白夜啊？"));
        // 返回扩展后的查询列表
        return queries;
    }
}