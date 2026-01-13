package com.baiye.baiyeaiagent.controller;

import com.baiye.baiyeaiagent.agent.BaiManus;
import com.baiye.baiyeaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * ClassName: AiController
 * Package: com.baiye.baiyeaiagent.controller
 * Description: AI相关接口
 *
 * @Author 白夜
 * @Create 2026/1/9 22:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用 AI 恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用（第一种方法）
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }


    //SSE 流式调用 AI 恋爱大师应用（第二种方法）
    //@GetMapping(value = "/love_app/chat/sse")
    //public Flux<ServerSentEvent<String>> doChatWithLoveAppSSE(String message, String chatId) {
    //    return loveApp.doChatByStream(message, chatId)
    //            .map(chunk -> ServerSentEvent.<String>builder()
    //                    .data(chunk)
    //                    .build());
    //}



    //SSE 流式调用 AI 恋爱大师应用（第三种方法）
    //@GetMapping("/love_app/chat/sse/emitter")
    //public SseEmitter doChatWithLoveAppSseEmitter(String message, String chatId) {
    //    // 创建一个超时时间较长的 SseEmitter
    //    SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
    //    // 获取 Flux 数据流并直接订阅
    //    loveApp.doChatByStream(message, chatId)
    //            .subscribe(
    //                    // 处理每条消息
    //                    chunk -> {
    //                        try {
    //                            emitter.send(chunk);
    //                        } catch (IOException e) {
    //                            emitter.completeWithError(e);
    //                        }
    //                    },
    //                    // 处理错误
    //                    emitter::completeWithError,
    //                    // 处理完成
    //                    emitter::complete
    //            );
    //    // 返回emitter
    //    return emitter;
    //}


    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        BaiManus baiManus = new BaiManus(allTools, dashscopeChatModel);
        return baiManus.runStream(message);
    }



}

