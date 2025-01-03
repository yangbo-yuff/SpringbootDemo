package com.yb.yff.game.service.impl;

import com.yb.yff.game.service.IWSEventService;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import com.yb.yff.sb.flux.server.service.IWSMessageListener;
import com.yb.yff.sb.flux.server.service.IWSServerManager;
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
 * @Class: RouterServiceImpl
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 业务路由服务
 */
@Service
@Slf4j
public class WSEventServiceImpl implements IWSEventService, IWSMessageListener {

	@Autowired
	IWSServerManager wsServerManager;

	@PostConstruct
	private void init() {
		// 添加事件监听器
		wsServerManager.addWSMessageListener(this);
	}

	/**
	 * 由实现者决定行为
	 * 网关:  转发业务到其它服务器
	 * 非网关:  执行业务逻辑
	 *
	 * @param requestDTO
	 */
	@Override
	public void onMessage(WebSocketSession session, GameMessageEnhancedReqDTO requestDTO) {
		log.info("=========== ChatServer onMessage : " + requestDTO.getName());
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
	public void OnRouterFromServer2Client(GameMessageResDTO gameMessageServerDTO) {
		// TODO
	}

	/**
	 * 向指定客户端发送消息
	 *
	 * @param
	 * @param sessionID
	 * @param message
	 */
	@Override
	public void sendMessage(String sessionID, String message) {

	}
}
