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
 * @Description: 游戏消息体
 */

@Data
public class GameMessageReqDTO {
	/**
	 * 数据体
	 */
	private Object msg;

	/**
	 * 业务名称
	 */
	private String name;

	/**
	 * 消息的序号
	 */
	private Integer seq;
}
