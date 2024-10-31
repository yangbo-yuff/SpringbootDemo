package com.yb.yff.game.service;

import com.yb.yff.sb.data.dto.GameMessageReqDTO;
import com.yb.yff.sb.data.dto.GameMessageResDTO;

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
 * @Class: IWSClientService
 * @CreatedOn 2024/10/12.
 * @Email: yangboyff@gmail.com
 * @Description: Websocket客户端服务 接口
 */
public interface IWSClientService {
	/**
	 * 向指定服务器发送消息
	 *
	 * @param sessionID     请求源Client与gate链接的session id
	 * @param typeName      业务类型，根据此类型，找到对应的业务服务器，转发消息
	 * @param fromClientDTO
	 */
	void sendMessage(String sessionID, String typeName, GameMessageReqDTO fromClientDTO);

	/**
	 * Http Get 请求
	 *
	 * @param typeName      业务类型，根据此类型，找到对应的业务服务器，转发消息
	 * @param fromClientDTO
	 * @param callback
	 */
	void sendGetHttpRequest(String typeName, GameMessageReqDTO fromClientDTO, Consumer<GameMessageResDTO> callback);
}
