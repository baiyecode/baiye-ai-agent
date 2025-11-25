package com.baiye.baiyeaiagent.demo.invoke;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * ClassName: HttpAiInvoke
 * Package: com.baiye.baiyeaiagent.demo.invoke
 * Description: Http调用 AI大模型
 *
 * @Author 白夜
 * @Create 2025/11/25 20:36
 * @Version 1.0
 */

public class HttpAiInvoke {

    public static void main(String[] args) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
        String apiKey = TestApiKey.API_KEY; // 确保环境变量已设置

        // --- 修复部分开始 ---
        JSONObject body = JSONUtil.createObj()
                .set("model", "qwen-plus")
                .set("input", JSONUtil.createObj()
                        .set("messages", JSONUtil.createArray()
                                // 关键修改：使用 .put() 支持链式调用，不要用 .add()
                                .put(JSONUtil.createObj()
                                        .set("role", "system")
                                        .set("content", "You are a helpful assistant."))
                                .put(JSONUtil.createObj()
                                        .set("role", "user")
                                        .set("content", "你是谁？"))
                        )
                )
                .set("parameters", JSONUtil.createObj()
                        .set("result_format", "message")
                );

        try (HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .timeout(30000)
                .execute()) {

            System.out.println("响应结果: " + response.body());
        }
    }
}
