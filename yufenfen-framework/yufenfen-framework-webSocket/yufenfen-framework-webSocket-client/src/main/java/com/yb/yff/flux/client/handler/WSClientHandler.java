package com.yb.yff.flux.client.handler;

import com.yb.yff.flux.client.data.dto.ServerInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;

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
public class WSClientHandler {

	public void createClient(ServerInfoDTO serverInfo, IWSClientEventListener clientEventListener) {
		// 服务器名称
		String clientName = serverInfo.getNodeName();

		// 创建 WebSocket 客户端
		ReactorNettyWebSocketClient webSocketclient = new ReactorNettyWebSocketClient();

		// WebSocket 服务器的 URI
		URI serverUri = URI.create(serverInfo.getNode());

		connectToServer(webSocketclient, clientName, serverUri, clientEventListener)
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe();
	}

	private Mono<Void> connectToServer(ReactorNettyWebSocketClient webSocketclient, String clientName, URI serverUri,
	                                   IWSClientEventListener clientEventListener) {
		log.info("=========== Start conect to Server: " + serverUri);

		// 连接到 WebSocket 服务器，发送并接收消息
		return webSocketclient.execute(serverUri, session -> {
			// 与服务器建立链接
			clientEventListener.onConnect(clientName, session);

			// 监听收到服务器发送的消息
			session.receive()
//					.map(WebSocketMessage::getPayloadAsText)
					.doOnNext(message -> {
						clientEventListener.onMessage(session, message);
					})
					.doOnError(error -> {
						clientEventListener.onError(session, error);
					})
					.doOnComplete(() -> {
						clientEventListener.onDisconnect(session);
					})
					.doFinally(signalType -> {
						clientEventListener.onDisconnect(clientName, session, signalType);
					})
					.subscribe();

			// 心跳逻辑保持连接活跃
			Flux.interval(Duration.ofSeconds(30))
					.flatMap(i -> session.send(Mono.just(session.textMessage("PING"))))
					.doOnError(error -> {
						log.error("Heartbeat error: " + error.getMessage());
					})
					.subscribe();

			// 连接建立后暂时不处理消息
			return Mono.never(); // 保持连接
		});
	}
}
