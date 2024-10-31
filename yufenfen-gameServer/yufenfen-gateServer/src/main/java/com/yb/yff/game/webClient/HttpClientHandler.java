package com.yb.yff.game.webClient;

import com.alibaba.fastjson.JSON;
import com.yb.yff.game.utils.HttpRequestUtils;
import com.yb.yff.sb.data.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

/**
 * Copyright (c) 2024 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: HttpClientHandler
 * @CreatedOn 2024/10/13.
 * @Email: yangboyff@gmail.com
 * @Description: Websocket 发送Http请求处理器，非异步
 */
@Component
@Slf4j
public class HttpClientHandler {
	@Autowired
	private WebClient webClient;

	/**
	 * 发送 get 请求
	 *
	 * @param businessURL
	 * @param message
	 * @return
	 */
	public Mono<ResponseDTO> getHttpRequest(String businessURL, String message) {
		return Mono.fromCallable(() -> {
			URI uri = HttpRequestUtils.jsonStr2Uri(businessURL, message);
			return webClient.get()
					.uri(uri)
					.retrieve()
					.bodyToMono(ResponseDTO.class)
					.block();
		}).subscribeOn(Schedulers.boundedElastic()); // 移到单独的线程池
	}

	/**
	 * 发送 post 请求
	 *
	 * @param businessURL
	 * @param message
	 * @return
	 */
	public ResponseDTO postHttpRequest(String businessURL, String message) {
		Object object = JSON.parseObject(message);
		return webClient.post()
				.uri(businessURL)
				.bodyValue(object)
				.retrieve()
				.bodyToMono(ResponseDTO.class)
				.doOnError(error -> {
					System.err.println("Failed to send HTTP request: " + error.getMessage());
				})
				.block();
	}

	/**
	 * 发送 put 请求
	 *
	 * @param businessURL
	 * @param message
	 * @return
	 */
	public ResponseDTO putHttpRequest(String businessURL, String message) {
		Object object = JSON.parseObject(message);

		return webClient.put()
				.uri(businessURL)
				.bodyValue(object)
				.retrieve()
				.bodyToMono(ResponseDTO.class)
				.doOnError(error -> {
					System.err.println("Failed to send HTTP request: " + error.getMessage());
				})
				.block();
	}

	/**
	 * 发送 delete 请求
	 *
	 * @param businessURL
	 * @param message
	 * @return
	 */
	public ResponseDTO deleteHttpRequest(String businessURL, String message) {
		URI uri = HttpRequestUtils.jsonStr2Uri(businessURL, message);

		return webClient.delete()
				.uri(uri)
				.retrieve()
				.bodyToMono(ResponseDTO.class)
				.doOnError(error -> {
					System.err.println("Failed to send HTTP request: " + error.getMessage());
				})
				.block();
	}
}
