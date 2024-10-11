package com.yb.yff.game.myWebSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

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
 * @Class: MyWebSocketHandler
 * @CreatedOn 2024/10/4.
 * @Email: yangboyff@gmail.com
 * @Description: 消息处理器
 */

@Component
public interface MyWebSocketHandler extends WebSocketHandler {
    /**
     * 客户端连接
     *
     * @param session
     */
    public void onConnect(WebSocketSession session);

    /**
     * 客户端断开连接
     *
     * @param session
     * @param signalType
     */
    public void onDisconnect(WebSocketSession session, SignalType signalType);

    /**
     * 处理客户端发送的消息
     *
     * @param session
     * @param message
     */
    public void onMessage(WebSocketSession session, String message);
}
