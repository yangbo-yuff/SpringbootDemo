package com.yb.yff.game.service.impl;

import com.yb.yff.flux.server.service.IWSMessageListener;
import com.yb.yff.flux.server.service.IWSServerManager;
import com.yb.yff.game.service.IWSClientService;
import com.yb.yff.game.service.IWSRouterService;
import com.yb.yff.sb.constant.WSNetConstant;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.data.dto.GameMessageReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class WSRouterServiceImpl implements IWSRouterService, IWSMessageListener {
	public static final String TAG = "=========== GateServer";

	@Autowired
	IWSServerManager wsServerManager;

	@Autowired
	IWSClientService wsClientService;

	@PostConstruct
	private void init() {
		// 添加事件监听器
		wsServerManager.addWSMessageListener(this);
	}


	/**
	 * 由实现者决定行为
	 * 网关:  转发业务到其它服务器
	 * 非网关:  执行业务逻辑
	 * @param session
	 * @param requestDTO
	 */
	@Override
	public void onMessage(WebSocketSession session, GameMessageEnhancedReqDTO requestDTO) {
		String requestName = requestDTO.getName();
		log.info(TAG + " onMessage requestName : " + requestName);

		String[] names = requestName.split("\\.");

		if (names.length != 2){
			log.info(TAG + " onMessage 非法 requestName : " + requestName);
			return;
		}

		String typeName = names[0];

		// 账户请求 HTTP请求账户服务器
		if(typeName.equals(WSNetConstant.SERVER_TYPE_ACCOUNT)){
		    accountBusiness(session.getId(), typeName, requestDTO);
			return;
		}

		// 暂时业务服务器只有一个，slg，后续会分化出多个业务服务器，就不用处理服务器name逻辑了
		if(typeName.equals(WSNetConstant.SERVER_TYPE_CHAT)){
			typeName = WSNetConstant.SERVER_TYPE_CHAT;
		} else {
			typeName = WSNetConstant.SERVER_TYPE_SLG;
		}

		// 其它请求，转发SLG逻辑服务器/聊天服务器
		wsClientService.sendMessage(session.getId(), typeName, requestDTO);
	}

	/**
	 * 向指定客户端发送消息
	 *
	 * @param message
	 */
	@Override
	public void sendMessage(GameMessageEnhancedResDTO message) {

		GameMessageResDTO gameMessageResDTO = new GameMessageResDTO();
		BeanUtils.copyProperties(message, gameMessageResDTO);

		wsServerManager.sendMessage(message.getSessionClient2Gate(), gameMessageResDTO);
	}

	/**
	 * 账户业务, http非阻塞请求，拿到响应结果后，直接向发送响应结果消息
	 * @param sessionId
	 * @param typeName
	 * @param requestDTO
	 */
	private void accountBusiness(String sessionId, String typeName, GameMessageReqDTO requestDTO){
		wsClientService.sendGetHttpRequest(typeName, requestDTO, gameMessageResDTO -> {
			wsServerManager.sendMessage(sessionId, gameMessageResDTO);
		});
	}
}
