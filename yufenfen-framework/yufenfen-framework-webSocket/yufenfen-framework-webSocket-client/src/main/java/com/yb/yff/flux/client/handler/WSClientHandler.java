package com.yb.yff.flux.client.handler;

import com.yb.yff.flux.client.data.dto.ServerInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: WSClientConfig
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: WebSocket 客户端管理
 */
@Slf4j
public class WSClientHandler implements IWSClientEventHandler {
	private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	public void connectToServer(ServerInfoDTO serverInfo) {
		// 创建 WebSocket 客户端
		ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

		// WebSocket 服务器的 URI
		URI serverUri = URI.create(serverInfo.getNode());

		// 连接到 WebSocket 服务器，发送并接收消息
		client.execute(serverUri, session -> {
			// 与服务器建立链接
			this.onConnect(session);
			sessionMap.put(serverInfo.getNodeName(), session);

			// 监听收到服务器发送的消息
			session.receive()
					.map(WebSocketMessage::getPayloadAsText)
					.doOnNext(message -> {
						this.onMessage(session, message);
					})
					.doOnError(error -> {
						this.onError(session, error);
					})
					.doOnComplete(() -> {
						this.onDisconnect(session);
					})
					.doFinally(signalType -> {
						this.onDisconnect(session, signalType);
						sessionMap.remove(serverInfo.getNodeName(), session);
					})
					.subscribe();

			// 心跳逻辑保持连接活跃
			Flux.interval(Duration.ofSeconds(30))
					.doOnEach(i -> session.send(Mono.just(session.textMessage("PING"))).subscribe())
					.subscribe();

			// 连接建立后暂时不处理消息
			return Mono.empty();
		}).subscribe();
	}


	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	@Override
	public void onConnect(WebSocketSession session) {
		log.info("=========== Server-" + session.getId() + " 已链接!");
	}

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 * @param signalType
	 */
	@Override
	public void onDisconnect(WebSocketSession session, SignalType signalType) {
		log.info("=========== Server-" + session.getId() + " 断开链接!");

	}

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	@Override
	public void onDisconnect(WebSocketSession session) {
		log.info("=========== Server-" + session.getId() + " 断开链接!");

	}

	/**
	 * 错误
	 *
	 * @param error
	 */
	@Override
	public void onError(WebSocketSession session, Throwable error) {
		log.info("=========== Server-" + session.getId() + " Error: " + error.getMessage());

	}

	/**
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	@Override
	public void onMessage(WebSocketSession session, String message) {
		log.info("=========== 收到 Server-" + session.getId() + "的消息: " + message);

	}

	/**
	 * 主动关闭连接
	 *
	 * @param nodeName
	 */
	public void closeConnection(String nodeName) {
		WebSocketSession session = sessionMap.remove(nodeName);
		if (session != null) {
			session.close().subscribe();
			log.info("====== Connection to " + nodeName + " closed.");
		}
	}

	/**
	 * 向服务器发送消息
	 *
	 * @param nodeName
	 * @param message
	 */
	public void onSendMessage(String nodeName, String message) {
		WebSocketSession session = sessionMap.get(nodeName);
		if (session != null) {
			session.send(Mono.just(session.textMessage(message))).subscribe();
		} else {
			log.info("====== No active session for serverUri: " + nodeName);
		}
	}
}
