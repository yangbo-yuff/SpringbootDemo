package com.yb.yff.robot.service.ros;

/**
 * Copyright (c) 2025 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: IRosListener
 * @CreatedOn 2025/1/2.
 * @Email: yangboyff@gmail.com
 * @Description: ros Topic 监听
 */
public interface IRosListener {
	/**
	 * 接收到消息
	 * @param message
	 */
	void onMessage(String message);
}
