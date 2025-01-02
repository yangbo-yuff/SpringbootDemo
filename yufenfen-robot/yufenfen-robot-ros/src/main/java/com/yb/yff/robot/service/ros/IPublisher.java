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
 * @Class: IPublisher
 * @CreatedOn 2025/1/2.
 * @Email: yangboyff@gmail.com
 * @Description: ros topic 发送器
 */
public interface IPublisher {
    /**
     * 发送消息
     *
     * @param data
     */
    void publish(String data);
}
