package com.yb.yff.game.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.constant.GlobalString;
import com.yb.yff.game.service.IWSClientService;
import com.yb.yff.game.service.IWSRouterService;
import com.yb.yff.game.webClient.HttpClientHandler;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.data.dto.GameMessageReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import com.yb.yff.sb.flux.client.service.IWSCLientsManager;
import com.yb.yff.sb.flux.client.service.IWSMessageListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;


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
public class WSClientServiceImpl implements IWSClientService, IWSMessageListener {

	@Autowired
	HttpClientHandler httpClientHandler;

	@Autowired
	IWSRouterService wsRouterService;

	@Autowired
	IWSCLientsManager wSClientsManager;

	/**
	 * 账户服务器地址
	 */
	private String accountServerUrl;

	@PostConstruct
	private void init(){
		wSClientsManager.addWSMessageListener(this);
	}

	/**
	 * 向指定服务器发送消息
	 *
	 * @param sessionID     请求源Client与gate链接的session id
	 * @param typeName      业务类型，根据此类型，找到对应的业务服务器，转发消息
	 * @param fromClientDTO
	 */
	@Override
	public void sendMessage(String sessionID, String typeName, GameMessageReqDTO fromClientDTO) {
		log.info("*********** Gate sendMessage typeName : " + typeName);

		GameMessageEnhancedReqDTO message = new GameMessageEnhancedReqDTO();
		BeanUtils.copyProperties(fromClientDTO, message);
		message.setSessionClient2Gate(sessionID);

		wSClientsManager.sendMessage(typeName, JSONObject.toJSONBytes(message));
	}

	/**
	 * 发送http Get 请求
	 *
	 * @param typeName      业务类型，根据此类型，找到对应的业务服务器，转发消息
	 * @param fromClientDTO
	 * @return
	 */
	@Override
	public void sendGetHttpRequest(String typeName, GameMessageReqDTO fromClientDTO, Consumer<GameMessageResDTO> callback) {
		if (accountServerUrl == null) {
			accountServerUrl = wSClientsManager.connectToBusinessServer();
		}

		String businessName = GlobalString.ACCOUNT_BUSINESS_BASE + typeName;

		String jonStr = JSONObject.toJSONString(fromClientDTO.getMsg());
		httpClientHandler.getHttpRequest(accountServerUrl + businessName, jonStr)
				.doOnSuccess(responseDTO -> {
					GameMessageResDTO gameMessageResDTO = new GameMessageResDTO();
					gameMessageResDTO.setName(fromClientDTO.getName());
					gameMessageResDTO.setSeq(fromClientDTO.getSeq());
					gameMessageResDTO.setCode(responseDTO.getCode());
					gameMessageResDTO.setMsg(responseDTO.getData());

					callback.accept(gameMessageResDTO);
				})
				.doOnError(error -> {
					log.info("Failed to send HTTP request: " + error.getMessage());
				})
				.publishOn(Schedulers.boundedElastic()) // 确保回调在单独的线程池中执行
				.subscribe();
	}

	/**
	 * * 由实现者决定行为：
	 * * 网关:  转发业务到其它服务器
	 * * 非网关:  执行业务逻辑
	 *
	 * @param session
	 * @param responseDTO
	 */
	@Override
	public void onMessage(WebSocketSession session, GameMessageEnhancedResDTO responseDTO) {
		// 响应客户端数据
//		GameMessageEnhancedResDTO gameMessageResDTO = new GameMessageEnhancedResDTO();
//		BeanUtils.copyProperties(responseDTO, gameMessageResDTO);
//		gameMessageResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		wsRouterService.sendMessage(responseDTO);
	}
}
