package com.yb.yff.sb.flux.server.service;

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
public interface IWSServerManager extends IWSServerSender {
	/**
	 * 添加消息监听
	 * @param listener
	 */
	void addWSMessageListener(IWSMessageListener listener);
}
