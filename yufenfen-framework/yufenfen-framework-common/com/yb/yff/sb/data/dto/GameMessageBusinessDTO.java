package com.yb.yff.sb.data.dto;

import lombok.Data;

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
 * @Class: MyGameMessage
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏消息体, 业务服务器业务处理
 */

@Data
public class GameMessageBusinessDTO extends GameMessageEnhancedReqDTO {
	/**
	 * 网关服务器链接到业务服务器的sessionID,辅助业务服务器完成业务后，向网关相应数据
	 */
	private String seesionGate2Business;
}
