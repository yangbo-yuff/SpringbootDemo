package com.yb.yff.flux.client.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.flux.client.data.GBServerInfos;
import com.yb.yff.flux.client.data.dto.ServerInfoDTO;
import com.yb.yff.flux.client.handler.IWSClientEventListener;
import com.yb.yff.flux.client.handler.WSClientHandler;
import com.yb.yff.flux.client.service.IWSCLientsManager;
import com.yb.yff.flux.client.service.IWSMessageListener;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.ArrayList;
import java.util.List;
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
 * @Class: WSClientsManagerImpl
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: websocket 客户端管理者
 */
@Component
@Slf4j
public class WSClientsManagerImpl implements IWSClientEventListener, IWSCLientsManager {
	@Autowired
	GBServerInfos gbServerInfos;

	WSClientHandler wsClientHandler;

	/**
	 * 消息监听器, 管理器收到到消息，通知所有监听器
	 */
	private List<IWSMessageListener> wsMessageListeners = new ArrayList<>();


	private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();


	/**
	 * 添加消息监听
	 *
	 * @param listener
	 */
	@Override
	public void addWSMessageListener(IWSMessageListener listener) {
		wsMessageListeners.add(listener);

		if(wsClientHandler == null){
			wsClientHandler = new WSClientHandler();
		}
	}

	/**
	 * 连接到，默认每个集群的一个服务，后期需要调整为：
	 * 1. 账号服务，游戏服务，聊天服务，都通过负载均衡分配
	 */
	@Override
	public String connectToBusinessServer() {

		// 初始化账户服务信息
		ServerInfoDTO accountServer = gbServerInfos.getAccounts().get(0);
		String accountServerUrl = accountServer.getNode();

		// 链接游戏服务
		ServerInfoDTO gameServer = gbServerInfos.getGames().get(0);
		if(!sessionMap.containsKey(gameServer.getNodeName())){
			wsClientHandler.createClient(gameServer, this);
		}

		// 链接聊天服务
		ServerInfoDTO chatServer = gbServerInfos.getChats().get(0);
		if(!sessionMap.containsKey(chatServer.getNodeName())){
			wsClientHandler.createClient(chatServer, this);
		}

		return accountServerUrl;
	}

	/**
	 * 发送消息
	 *
	 * @param clientName
	 * @param data
	 */
	@Override
	public void sendMessage(String clientName, byte[] data) {
		WebSocketSession session = getSession(clientName);

		if (session == null) {
			log.info("====== 正在链接到服务器: " + clientName + " 请稍后！");
			connectToBusinessServer();
			return;
		}

		sendMessage(session, data);
	}


	/**
	 * 客户端连接
	 *
	 * @param session
	 */
	@Override
	public void onConnect(String clientName, WebSocketSession session) {
		sessionMap.put(clientName, session);
		log.info("=========== Server-" + session.getId() + " 已链接!");
	}

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 * @param signalType
	 */
	@Override
	public void onDisconnect(String clientName, WebSocketSession session, SignalType signalType) {
		sessionMap.remove(clientName, session);
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
	 * 主动关闭连接
	 *
	 * @param nodeName
	 */
	@Override
	public void closeConnection(String nodeName) {
		WebSocketSession session = sessionMap.remove(nodeName);
		if (session != null) {
			session.close().subscribe();
			log.info("====== Connection to " + nodeName + " closed.");
		}
	}

	/**
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	@Override
	public void onMessage(WebSocketSession session, WebSocketMessage message) {
		String josnStr = message.getPayloadAsText();
		log.info("=========== 收到 Server-" + session.getId() + "的消息: " + josnStr);

		GameMessageEnhancedReqDTO requestDTO = JSONObject.parseObject(josnStr, GameMessageEnhancedReqDTO.class);

		wsMessageListeners.stream().forEach(listener -> listener.onMessage(session, requestDTO));
	}


	/**
	 * 向服务器发送消息
	 *
	 * @param session
	 * @param message
	 */
	public void sendMessage(WebSocketSession session, byte[] message) {
		// 发送
		session.send(
				Mono.just(
						session.binaryMessage(data -> data.wrap(message))
				)
		).subscribe();
	}

	private WebSocketSession getSession(String nodeName) {
		return sessionMap.get(nodeName);
	}
}
