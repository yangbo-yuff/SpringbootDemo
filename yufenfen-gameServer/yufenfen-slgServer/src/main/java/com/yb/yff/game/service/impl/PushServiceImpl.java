package com.yb.yff.game.service.impl;

import com.yb.yff.flux.server.service.IWSServerSender;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.game.service.IPushSessionManager;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
 * @Class: IPushService
 * @CreatedOn 2024/11/13.
 * @Email: yangboyff@gmail.com
 * @Description: 向客户端广播消息服务
 */
@Service
@Slf4j
public class PushServiceImpl implements IPushService, IPushSessionManager {

	@Autowired
	private IWSServerSender iwsServerSender;

	/**
	 * roleID 与 sessionID 的映射
	 */
	private final Map<Integer, String> roleSessions = new ConcurrentHashMap<>();

	/**
	 * roleID 与 clientSessionID 的映射
	 */
	private final Map<Integer, String> roleclientSessions = new ConcurrentHashMap<>();

	/**
	 * 添加客户端session
	 *
	 * @param rid
	 * @param sessionID 逻辑服务与网关的sessionID
	 * @param clientSessionID 网关与客户端的sessionID
	 */
	@Override
	public void addSession(Integer rid, String sessionID, String clientSessionID) {
		log.info("Adding session for role ID: {} - Session ID: {}", rid, sessionID);
		roleSessions.put(rid, sessionID);
		roleclientSessions.put(rid, clientSessionID);
	}

	/**
	 * 广播消息
	 *
	 * @param message
	 */
	@Override
	public void broadcastMessage(GameMessageEnhancedResDTO message) {
		roleSessions.forEach((rid, sessionID) -> {
			log.info("Broadcasting message to session: {}", sessionID);

			String client2GateSessionID = roleclientSessions.get(rid);
			message.setSessionClient2Gate(client2GateSessionID);

			sendMessage(sessionID, message)
					.subscribe();
		});
	}

	/**
	 * 向指定客户端发送消息
	 *
	 * @param rid
	 * @param message
	 */
	@Override
	public void sendMessage(Integer rid, GameMessageEnhancedResDTO message) {
		if (!roleSessions.containsKey(rid)) {
			return;
		}

		String sessionID = roleSessions.get(rid);

		String client2GateSessionID = roleclientSessions.get(rid);
		message.setSessionClient2Gate(client2GateSessionID);

		sendMessage(sessionID, message)
				.subscribe();
	}

	private Mono<Void> sendMessage(String sessionID, GameMessageResDTO message) {
		return Mono.fromRunnable(() -> {
					try {
						iwsServerSender.sendMessage(sessionID, message);
					} catch (Exception e) {
						log.error("Error sending message to session: {}", sessionID, e);
					}
				})
				.subscribeOn(Schedulers.boundedElastic())
				.then();
	}
}
