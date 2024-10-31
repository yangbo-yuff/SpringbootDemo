package com.yb.yff.flux.server.handler;

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
 * @Class: IWSServerHandler
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: websocket server 链接处理器
 */
public interface IWSServerHandler {
	/**
	 * 添加事件监听器
	 *
	 * @param listener
	 */
	void setWebSocketEventListener(IWSEventListener listener);
}
