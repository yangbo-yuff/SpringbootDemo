package com.yb.yff.game.service;

import com.yb.yff.sb.data.dto.GameMessageResDTO;

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
 * @Class: IWSRouterService
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 业务路由接口
 */
public interface IWSEventService {
	/**
	 * 业务路由处理, 分发客户端请求到各个业务服务器
	 * @param message
	 */
	void OnRouterFromClient2Server(String message);

	/**
	 * 业务路由处理, 业务服务器处理结果返回给业务发起端（客户端）
	 * @param gameMessageServerDTO
	 */
	void OnRouterFromServer2Client(GameMessageResDTO gameMessageServerDTO);

	/**
	 * 向指定客户端发送消息
	 * @param
	 * @param sessionID
	 * @param message
	 */
	void sendMessage(String sessionID, String message);
}
