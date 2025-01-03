package com.yb.yff.sb.flux.client.service;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
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
 * @Class: MessageListener
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: Websocket消息监听器接口
 */
public interface IWSMessageListener {
	/**
	 * * 由实现者决定行为：
	 * * 网关:  转发业务到其它服务器
	 * * 非网关:  执行业务逻辑
	 *
	 * @param session
	 * @param responseDTO
	 */
//	@Override
	void onMessage(WebSocketSession session, GameMessageEnhancedResDTO responseDTO);
}
