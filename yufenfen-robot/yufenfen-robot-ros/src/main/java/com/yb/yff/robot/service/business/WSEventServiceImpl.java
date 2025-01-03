package com.yb.yff.robot.service.business;

import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.flux.server.service.IWSMessageListener;
import com.yb.yff.sb.flux.server.service.IWSServerManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
 * @Class: RouterServiceImpl
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 业务路由服务
 */
@Service
@Slf4j
public class WSEventServiceImpl implements IWSMessageListener {

	@Autowired
	IWSServerManager wsServerManager;

	@Autowired
	Map<String, IBusinessService> businessServiceList;

	public static final String TAG = "=========== Java ros Server";

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
	 * @param session
	 * @param requestDTO
	 */
	@Override
	public void onMessage(WebSocketSession session, GameMessageEnhancedReqDTO requestDTO) {
		String requestName = requestDTO.getName();
		log.info(TAG + " onMessage requestName : " + requestName);

		String[] names = requestName.split("\\.");

		if (names.length != 2) {
			log.info(TAG + " onMessage 非法 requestName : " + requestName);
			return;
		}

		IBusinessService businessService = getBusinessService(names[0]);
		if (businessService == null) {
			log.info(TAG + " onMessage 业务不存在 : " + requestName);
			return;
		}

		businessLogic(session.getId(), requestDTO, businessService, names[1])
				.then(Mono.fromRunnable(() -> {
					onFinishTask(requestDTO);
				}))
				.subscribe();
	}

	private Mono<GameMessageEnhancedResDTO> businessLogic(String sessionId, GameMessageEnhancedReqDTO requestDTO, IBusinessService businessService, String businessName) {
		return Mono.fromCallable(() -> businessService.dispathBusiness(businessName, requestDTO))
				.flatMap(resDTO -> Mono.just(resDTO))
				.subscribeOn(Schedulers.boundedElastic());
	}

	private IBusinessService getBusinessService(String typeName) {
		return businessServiceList.get(typeName);
	}


	/**
	 * 任务完成
	 *
	 * @param requestDTO
	 */
	private void onFinishTask(GameMessageEnhancedReqDTO requestDTO) {

	}
}
