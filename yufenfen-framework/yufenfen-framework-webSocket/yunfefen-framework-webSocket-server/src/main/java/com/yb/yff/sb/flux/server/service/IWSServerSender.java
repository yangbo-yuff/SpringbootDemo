package com.yb.yff.sb.flux.server.service;

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
 * @Class: IWSServerManager
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: websocket 服务器管理器
 */
public interface IWSServerSender {
	/**
	 * 向客户端发送消息
	 *
	 * @param sessionID
	 * @param message
	 */
	void sendMessage(String sessionID, GameMessageResDTO message);
}
