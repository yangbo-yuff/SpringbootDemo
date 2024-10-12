package com.yb.yff.game.service.impl;
import com.yb.yff.flux.client.data.dto.ServerInfoDTO;
import com.yb.yff.flux.client.handler.WSClientHandler;
import com.yb.yff.game.data.GBServerInfos;
import com.yb.yff.game.service.IWSClientService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

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
 * @Class: WSClientServiceImpl
 * @CreatedOn 2024/10/12.
 * @Email: yangboyff@gmail.com
 * @Description: WebSocket客户端服务
 */
@Service
@Slf4j
public class WSClientServiceImpl extends WSClientHandler implements IWSClientService {
	@Autowired
	GBServerInfos gbServerInfos;

	/**
	 * 初始化连接，默认每个集群的一个服务，后期需要调整为：
	 * 1. 账号服务，游戏服务，聊天服务，都通过负载均衡分配
	 */
	@PostConstruct
	private void connectToServer() {
//		// 链接账号服务
//		ServerInfoDTO accountServer = gbServerInfos.getAccounts().get(0);
//		this.connectToServer(accountServer);
//
//		// 链接游戏服务
//		ServerInfoDTO gameServer = gbServerInfos.getGames().get(0);
//		this.connectToServer(gameServer);
//
//		// 链接聊天服务
//		ServerInfoDTO chatServer = gbServerInfos.getChats().get(0);
//		this.connectToServer(chatServer);
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
	 * 向指定服务器发送消息
	 * @param
	 * @param sessionID
	 * @param message
	 */
	@Override
	public void sendMessage(String sessionID, String message){
		this.onSendMessage(sessionID, message);
	}
}
