package com.yb.yff.sb.flux.client.handler;

import org.springframework.web.reactive.socket.WebSocketMessage;
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

public interface IWSClientEventListener {

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	void onConnect(String clientName, WebSocketSession session);

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 * @param signalType
	 */
	void onDisconnect(String clientName, WebSocketSession session, SignalType signalType);

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
	 * 主动关闭连接
	 *
	 * @param nodeName
	 */
	void closeConnection(String nodeName);

	/**
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	void onMessage(WebSocketSession session, WebSocketMessage message);
}
