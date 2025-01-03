package com.yb.yff.sb.flux.server.handler.impl;

import com.yb.yff.sb.flux.server.handler.IWSEventListener;
import com.yb.yff.sb.flux.server.handler.IWSServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

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
 * @Class: WSServerHandler
 * @CreatedOn 2024/10/4.
 * @Email: yangboyff@gmail.com
 * @Description: 消息处理器
 */

@Component
@Slf4j
public class WSServerHandler implements WebSocketHandler, IWSServerHandler {
	private IWSEventListener wsEventListeners;

	/**
	 * 添加事件监听器
	 *
	 * @param listener
	 */
	@Override
	public void setWebSocketEventListener(IWSEventListener listener) {
		wsEventListeners = listener;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		log.info("=========== Start handle!");
		// 存储新连接的客户端
		wsEventListeners.onConnect(session);

		// 处理收到的消息
		Mono<Void> messageHandling = session.receive()
				.doOnNext(message -> {
					wsEventListeners.onMessage(session, message);
				})
				.doOnComplete(() -> {
					wsEventListeners.onDisconnect(session);
				})
				.doFinally(signalType -> {
					wsEventListeners.onDisconnect(session, signalType);
				})
				.then();

		return messageHandling;
	}

}
