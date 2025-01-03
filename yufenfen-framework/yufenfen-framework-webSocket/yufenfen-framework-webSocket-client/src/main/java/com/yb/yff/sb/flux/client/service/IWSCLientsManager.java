package com.yb.yff.sb.flux.client.service;

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
 * @Class: IWSCLientsManager
 * @CreatedOn 2024/10/31.
 * @Email: yangboyff@gmail.com
 * @Description: websocket 客户端管理
 */
public interface IWSCLientsManager {
	/**
	 * 添加消息监听
	 * @param listener
	 */
	void addWSMessageListener(IWSMessageListener listener);

	/**
	 * 连接到，默认每个集群的一个服务，后期需要调整为：
	 * 1. 账号服务，游戏服务，聊天服务，都通过负载均衡分配
	 * @return
	 */
	String connectToBusinessServer();

	/**
	 * 发送消息
	 * @param clientName
	 * @param data
	 */
	void sendMessage(String clientName, byte[] data);
}
