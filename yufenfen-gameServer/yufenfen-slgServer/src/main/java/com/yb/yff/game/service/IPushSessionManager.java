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
 * @Class: IPushService
 * @CreatedOn 2024/11/13.
 * @Email: yangboyff@gmail.com
 * @Description: 向客户端广播消息服务
 */
public interface IPushSessionManager {
	/**
	 * 添加客户端session
	 * @param rid
	 * @param sessionID 逻辑服务与网关的sessionID
	 * @param clientSessionID 网关与客户端的sessionID
	 */
	void addSession(Integer rid, String sessionID, String clientSessionID);
}
