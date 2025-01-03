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
 * @Description: 游戏消息体, 服务器向服务器请求，如，网格转发业务到业务服务器
 */

@Data
public class GameMessageEnhancedReqDTO extends GameMessageReqDTO {
	/**
	 * 普通的客户端到网关服务器的数据不包含此字段，gate到业务服务器的数据包含此字段；
	 * 客户端链接到网关服务器的sessionID,辅助网关在业务服务器完成业务后，向业务请求源相应数据
	 */
	private String sessionClient2Gate;

	/**
	 * 用户 ID
	 */
	private Integer uid;

	/**
	 * 角色 ID
	 */
	private Integer rid;
}
