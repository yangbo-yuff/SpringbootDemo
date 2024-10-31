package com.yb.yff.game.service;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;

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
	 * 向指定客户端返回处理结果
	 *
	 * @param gameMessageEnhancedResDTO
	 */
	void sendMessage(String sessionID, GameMessageEnhancedResDTO gameMessageEnhancedResDTO);
}
