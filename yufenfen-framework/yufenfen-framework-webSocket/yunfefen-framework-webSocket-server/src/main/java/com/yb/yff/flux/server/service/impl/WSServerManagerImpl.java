package com.yb.yff.flux.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.yff.flux.server.data.dto.HandshakeDTO;
import com.yb.yff.flux.server.handler.IWSEventListener;
import com.yb.yff.flux.server.handler.impl.WSServerHandler;
import com.yb.yff.flux.server.service.IWSMessageListener;
import com.yb.yff.flux.server.service.IWSServerManager;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.WSNetConstant;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import com.yb.yff.sb.utils.AesEncryptor;
import com.yb.yff.sb.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
 * @Class: IWSServerManager
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: websocket 服务器管理器
 */
@Service
@Slf4j
public class WSServerManagerImpl implements IWSServerManager, IWSEventListener {

	@Autowired
	WSServerHandler wsServerHandler;

	@Value("${game.server.check-secret}")
	boolean checkSecret;

	private List<IWSMessageListener> wsMessageListeners = new ArrayList<>();

	// 用于存储所有客户端的WebSocket会话
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		// 添加事件监听器
		wsServerHandler.setWebSocketEventListener(this);
	}

	/**
	 * 添加消息监听
	 *
	 * @param listener
	 */
	@Override
	public void addWSMessageListener(IWSMessageListener listener) {
		wsMessageListeners.add(listener);
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

		// 回复一个 握手
		Handshake(session);
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
		log.info("=========== Client-" + session.getId() + " 断开链接!  SignalType: " + signalType);
	}

	/**
	 * 客户端断开连接
	 *
	 * @param session
	 */
	@Override
	public void onDisconnect(WebSocketSession session) {
		// 当客户端断开连接时，移除客户端会话
		sessions.remove(session.getId());
		log.info("=========== Client-" + session.getId() + " 断开链接!");
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
	 * 处理客户端发送的消息
	 *
	 * @param session
	 * @param message
	 */
	@Override
	public void onMessage(WebSocketSession session, WebSocketMessage message) {
		String data = message.getPayloadAsText();
		log.info("=========== 收到 Client-" + session.getId() + "的消息");

		if (checkSecret) {
			// 获取自定义属性，没有则跳过解密
			Object secretKeyObj = session.getAttributes().get(WSNetConstant.SECRET_KEY);
			if (secretKeyObj == null) {
				log.info("=========== 没有发现加密key,需要先握手");
				Handshake(session);
			} else {
				//解密
				String secretKey = secretKeyObj.toString();
				log.info("=========== secretKey：" + secretKey);

				try {
					data = AesEncryptor.aesCBCDecrypt(data, secretKey);
				} catch (Exception e) {
					log.info("=========== 解密失败：");
					return;
				}
			}
		}

		// jsonstr to obj
		try {
			GameMessageEnhancedReqDTO gameMessageClientDTO = JSON.parseObject(data, GameMessageEnhancedReqDTO.class);

			// 排除心跳
			if (WSNetConstant.WS_MSG_HEARTBEAT.equals(gameMessageClientDTO.getName())) {
				return;
			}

			wsMessageListeners.stream().forEach(listener -> listener.onMessage(session, gameMessageClientDTO));
		} catch (Exception e){
			log.info("=========== 非法数据：" + data);
		}
	}

	/**
	 * 向客户端发送消息
	 *
	 * @param sessionID
	 * @param message
	 */
	@Override
	public void sendMessage(String sessionID, GameMessageResDTO message) {
		WebSocketSession session = sessions.get(sessionID);
		if (session != null) {
			sendMessage(session, message);
		} else {
			log.info("====== No active session for serverUri: " + sessions);
		}
	}

	/**
	 * 向客户端发送消息
	 *
	 * @param session
	 * @param message
	 */
	private void sendMessage(WebSocketSession session, GameMessageResDTO message) {

		if (checkSecret) {
			String secretKey = getSecretKey(session);

			try {
				byte[] messageBytes = JSONObject.toJSONBytes(message);
				// 加密
				String encryptStr = AesEncryptor.aesCBCEncrypt(messageBytes, secretKey);

				sendMessage(session, encryptStr.getBytes());
			} catch (Exception e) {
				log.info("====== Send message " + message.getName() + " Encrypt error: " + e.getMessage());
			}
			return;
		}

		sendMessage(session, JSONObject.toJSONBytes(message));
	}

	/**
	 * @param session
	 * @param message
	 */
	private void sendMessage(WebSocketSession session, byte[] message) {
		// 发送
		session.send(
				Mono.just(
						session.binaryMessage(data -> data.wrap(message))
				)
		).subscribe();
	}

	/**
	 * 握手
	 *
	 * @param session
	 */
	private void Handshake(WebSocketSession session) {
		if(!checkSecret){
			return;
		}

		String secretKey = getSecretKey(session);

		HandshakeDTO handshake = new HandshakeDTO();
		handshake.setKey(secretKey);

		GameMessageResDTO responseDTO = new GameMessageResDTO();
		responseDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		responseDTO.setMsg(handshake);
		responseDTO.setName(WSNetConstant.WS_MSG_HANDSHAKE);

		sendMessage(session, JSONObject.toJSONBytes(responseDTO));
	}


	private String getSecretKey(WebSocketSession session) {
		if (!checkSecret) {
			return null;
		}

		String secretKey = null;
		Object secretKeyObj = session.getAttributes().get(WSNetConstant.SECRET_KEY);
		if (secretKeyObj != null) {
			secretKey = secretKeyObj.toString();
		} else {
			secretKey = StringUtils.createStrBySize(16);
			session.getAttributes().put(WSNetConstant.SECRET_KEY, secretKey);
		}

		return secretKey;
	}
}
