package com.baiye.baiyeaiagent.app;

import com.baiye.baiyeaiagent.advisor.MyLoggerAdvisor;
import com.baiye.baiyeaiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: LoveApp
 * Package: com.baiye.baiyeaiagent.app
 * Description:
 *
 * @Author 白夜
 * @Create 2025/11/26 20:55
 * @Version 1.0
 */
@Component
@Slf4j
public class LoveApp {

    //https://docs.spring.io/spring-ai/reference/api/chat-memory.html#_memory_in_chat_client

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 1. 初始化 ChatClient 对象
     * 使用构造器注入 dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 创建基于内存的对话记忆，默认使用 InMemoryChatMemoryRepository

        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        FileBasedChatMemory fileBasedChatMemory = new FileBasedChatMemory(fileDir);
        // MessageWindowChatMemory 用于管理消息窗口，防止上下文过长
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(fileBasedChatMemory)
                .maxMessages(10) // 默认最大存储消息数，防止内存溢出
                .build();

        // 初始化 ChatClient
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                // 指定默认的系统 Prompt
                .defaultSystem(SYSTEM_PROMPT)
                // 指定基于内存的对话记忆 Advisor
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 添加自定义日志 Advisor,按需开启
                        new MyLoggerAdvisor()
                        // 添加自定义 Re2 Advisor,按需开启，会多消耗Token
                        //,new ReReadingAdvisor()
                )
                .build();
    }


    /**
     * 2. 编写对话方法
     * @param message 用户输入的对话内容
     * @param chatId 对话 ID
     * @return AI 的回复内容
     */
    public String chat(String message, String chatId) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(a -> a
                        // 指定对话 ID
                        .param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    //https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html#_bean_output_converter


    //定义恋爱报告类，Java14的record特性快速定义
    record LoveReport(String title, List<String> suggestions) {
    }


    /**
     * AI 恋爱报告功能（结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport chatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(a -> a
                        // 指定对话 ID
                        .param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }


    // AI 恋爱知识库问答功能

    //https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html#_questionansweradvisor

    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    /**
     * AI 恋爱知识库问答功能,查询增强
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用RAG知识库问答,查询增强
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                //应用RAG检索增强服务（基于云知识库）
                //.advisors(loveAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


}


