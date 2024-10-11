package com.yb.yff.game.myWebSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

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
@Slf4j
public class MyWebSocketHandlerImpl implements MyWebSocketHandler {

    // 用于存储所有客户端的WebSocket会话
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // 存储新连接的客户端
        onConnect(session);


        // 处理收到的消息
        Mono<Void> messageHandling = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    onMessage(session, message);
                })
                .doFinally(signalType -> {
                    onDisconnect(session, signalType);
                })
                .then();

        return messageHandling;
    }

    /**
     * 客户端断开连接
     *
     * @param session
     */
    @Override
    public void onConnect(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("=========== Client-" + session.getId() + " 已链接!");
    }

    /**
     * 客户端断开连接
     *
     * @param session
     * @param signalType
     */
    @Override
    public void onDisconnect(WebSocketSession session, SignalType signalType) {
        // 当客户端断开连接时，移除客户端会话
        sessions.remove(session.getId());
        log.info("=========== Client-" + session.getId() + " 断开链接!" );
    }

    /**
     * 处理客户端发送的消息
     *
     * @param session
     * @param message
     */
    @Override
    public void onMessage(WebSocketSession session, String message) {
        log.info("=========== 收到 Client-" + session.getId() + "的消息: " + message);

        // todo 处理消息
    }
}
