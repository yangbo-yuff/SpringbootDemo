package com.yb.yff.game.service.impl;

import com.yb.yff.game.data.dto.ws.GameMessageServerDTO;
import com.yb.yff.game.service.IWSRouterService;
import com.yb.yff.flux.server.handler.WSServerHandler;
import lombok.extern.slf4j.Slf4j;
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
 * @Class: RouterServiceImpl
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 业务路由服务
 */
@Service
@Slf4j
public class WSRouterServiceImpl extends WSServerHandler implements IWSRouterService {
	/**
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	@Override
	public void onMessage(WebSocketSession session, String message) {
		super.onMessage(session, message);

		log.info("=========== WSRouterServiceImpl 收到 Client-" + session.getId() + "的消息: " + message);

		// TODO
	}


	/**
	 * 业务路由处理, 分发客户端请求到各个业务服务器
	 *
	 * @param message
	 */
	@Override
	public void OnRouterFromClient2Server(String message) {
		// TODO 处理数据，解密，转为对象……
	}

	/**
	 * 业务路由处理, 业务服务器处理结果返回给业务发起端（客户端）
	 *
	 * @param gameMessageServerDTO
	 */
	@Override
	public void OnRouterFromServer2Client(GameMessageServerDTO gameMessageServerDTO) {
		// TODO
	}

	/**
	 * 向指定客户端发送消息
	 * @param
	 * @param sessionID
	 * @param message
	 */
	@Override
	public void sendMessage(String sessionID, String message){
		this.onSendMessage(sessionID, message);
	}
}
