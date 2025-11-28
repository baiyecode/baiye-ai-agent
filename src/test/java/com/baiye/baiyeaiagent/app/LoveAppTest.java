package com.baiye.baiyeaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是白夜";
        String answer = loveApp.chat(message, chatId);
        Assertions.assertNotNull(answer); //断言，answer 不为空
        // 第二轮
        message = "我想让另一半（Java）更爱我";
        answer = loveApp.chat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.chat(message, chatId);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是白夜，我想让另一半（叶瞬光）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.chatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

}
