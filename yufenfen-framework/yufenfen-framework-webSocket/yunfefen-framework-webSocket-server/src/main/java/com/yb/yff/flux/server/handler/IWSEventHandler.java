package com.yb.yff.flux.server.handler;

import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.SignalType;

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
 * @Class: WebSocketEventHandlerImpl
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: websocket event handler
 */

public interface IWSEventHandler {

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	void onConnect(WebSocketSession session);

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 * @param signalType
	 */
	void onDisconnect(WebSocketSession session, SignalType signalType);

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	void onDisconnect(WebSocketSession session);

	/**
	 * 错误
	 *
	 * @param error
	 */
	void onError(WebSocketSession session, Throwable error);

	/**
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	void onMessage(WebSocketSession session, String message);
}
