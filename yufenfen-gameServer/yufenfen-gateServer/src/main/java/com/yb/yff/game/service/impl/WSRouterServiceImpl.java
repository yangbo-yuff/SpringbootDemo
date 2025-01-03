package com.yb.yff.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.constant.GlobalString;
import com.yb.yff.game.service.IWSClientService;
import com.yb.yff.game.service.IWSRouterService;
import com.yb.yff.sb.constant.WSNetConstant;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.data.dto.GameMessageReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;
import com.yb.yff.sb.flux.server.service.IWSMessageListener;
import com.yb.yff.sb.flux.server.service.IWSServerManager;
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
		    accountBusiness(session, names[1], requestDTO);
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
	 * @param session
	 * @param typeName
	 * @param requestDTO
	 */
	private void accountBusiness(WebSocketSession session, String typeName, GameMessageReqDTO requestDTO){
		String sessionId = session.getId();

		if(typeName.contains(GlobalString.ACCOUNT_BUSINESS_LOGOUT)){
			addUserId2Logout(session, requestDTO);
		}

		wsClientService.sendGetHttpRequest(typeName, requestDTO, gameMessageResDTO -> {
			wsServerManager.sendMessage(sessionId, gameMessageResDTO);
			// 登录成功，缓存用户信息
			if(gameMessageResDTO.getName().contains(GlobalString.ACCOUNT_BUSINESS_LOGIN)){
				CacheUserId2Session(session, gameMessageResDTO);
			}
		});
	}

	/**
	 * 缓存用户ID
	 * @param session
	 * @param gameMessageResDTO
	 */
	private void CacheUserId2Session(WebSocketSession session, GameMessageResDTO gameMessageResDTO){
		String jsonString = JSON.toJSONString(gameMessageResDTO.getMsg());
		JSONObject jData = JSONObject.parseObject(jsonString);

		if(jData.containsKey(GlobalString.JSONOBJ_KEY_USER_ID)){
			Integer uid = jData.getInteger(GlobalString.JSONOBJ_KEY_USER_ID);

			session.getAttributes().put(GlobalString.JSONOBJ_KEY_USER_ID, uid);
		}
	}

	/**
	 * 添加用户ID到请求参数中
	 * @param session
	 * @param requestDTO
	 */
	private void addUserId2Logout(WebSocketSession session, GameMessageReqDTO requestDTO){
		Object msg = requestDTO.getMsg();
		if(msg == null){
			msg = new Object();
		}

		JSONObject reqJSONData = JSONObject.parseObject(msg.toString());


		Object uidObj = session.getAttributes().get(GlobalString.JSONOBJ_KEY_USER_ID);
		if (uidObj == null) {
			return;
		}

		Integer uid = Integer.parseInt(uidObj.toString());

		reqJSONData.put(GlobalString.JSONOBJ_KEY_USER_ID, uid);

		requestDTO.setMsg(reqJSONData);
	}

}
