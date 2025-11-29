package com.baiye.baiyeaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

/**
 * 自定义日志advisor
 * 可以打印info级别的日志，只输出单次用户提示词和AI回复的文本
 */
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {


	//https://docs.spring.io/spring-ai/reference/api/advisors.html#_examples

	private static final Logger logger = LoggerFactory.getLogger(MyLoggerAdvisor.class);

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() {
		return 0;
	}


	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		logRequest(chatClientRequest);

		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

		logResponse(chatClientResponse);

		return chatClientResponse;
	}

	@Override
	public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
												 StreamAdvisorChain streamAdvisorChain) {
		logRequest(chatClientRequest);

		Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);

		// 聚合响应
		return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponses, this::logResponse);
	}

	private void logRequest(ChatClientRequest request) {
		//logger.debug("request: {}", request);
		log.info("AI Request: {}", request.prompt().getUserMessage().getText());
		//return request;
	}

	private void logResponse(ChatClientResponse chatClientResponse) {
		//logger.debug("response: {}", chatClientResponse);
		log.info("AI Response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
	}

}