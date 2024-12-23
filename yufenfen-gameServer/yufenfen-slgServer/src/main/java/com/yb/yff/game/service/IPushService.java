package com.yb.yff.game.service;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
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
 * @Class: IPushService
 * @CreatedOn 2024/11/13.
 * @Email: yangboyff@gmail.com
 * @Description: 向客户端广播消息服务
 */
public interface IPushService {
	/**
	 * 广播消息
	 * @param message
	 */
	void broadcastMessage(GameMessageEnhancedResDTO message);

	/**
	 * 向指定客户端发送消息
	 * @param rid
	 */
	void sendMessage(Integer rid, GameMessageEnhancedResDTO message);
}
