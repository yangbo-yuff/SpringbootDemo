package com.yb.yff.sb.constant;

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
 * @Class: WSNetString
 * @CreatedOn 2024/10/18.
 * @Email: yangboyff@gmail.com
 * @Description: websocket 通信通用常量
 */
public class WSNetConstant {
	/**
	 * 服务器类型 ：逻辑服务
	 */
	public static final String SERVER_TYPE_ACCOUNT = "account";
	/**
	 * 服务器类型 ：聊天服务
	 */
	public static final String SERVER_TYPE_CHAT = "chat";
	/**
	 * 服务器类型 ：聊天服务
	 */
	public static final String SERVER_TYPE_SLG = "slg";



	public static final String SECRET_KEY = "secretKey";

	/**
	 * 握手
	 */
	public static final String WS_MSG_HANDSHAKE = "handshake";

	/**
	 * 心跳
	 */
	public static final String WS_MSG_HEARTBEAT = "heartbeat";
}
