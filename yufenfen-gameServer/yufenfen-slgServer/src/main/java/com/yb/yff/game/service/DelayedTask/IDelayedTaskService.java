package com.yb.yff.game.service.DelayedTask;


import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import com.yb.yff.sb.taskCallback.TimeConsumingTaskParam;

import java.util.function.Consumer;

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
 * @Class: RedisDelayedTaskService
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: Redis 延时任务服务
 */

public interface IDelayedTaskService {

	/**
	 * 添加一个延时任务
	 * @param task
	 * @param listener
	 */
	void addDelayedTask(TimeConsumingTask task, IDelayedTaskListener listener);

	/**
	 * 获取一个延时任务回调函数
	 *
	 * @param taskId
	 * @return
	 */
	Consumer<TimeConsumingTaskParam> getCallback(String taskId);

	/**
	 * 移除一个延时任务回调函数
	 *
	 * @param taskId
	 */
	void removeCallback(String taskId);

	/**
	 * 处理一个延时任务结果
	 *
	 * @param taskId
	 * @param resDTO
	 */
	void processResult(String taskId, GameMessageEnhancedResDTO resDTO);

	/**
	 * 获取一个延时任务的参数
	 *
	 * @param taskId
	 * @return
	 */
	TimeConsumingTaskParam getTaskParameter(String taskId);

	/**
	 * 获取一个延时任务的完成时返回给客户端的数据
	 *
	 * @param taskId
	 * @return
	 */
	GameMessageEnhancedResDTO getTask2ClientData(String taskId);

	/**
	 * 获取一个延时任务的监听者
	 *
	 * @param taskId
	 * @return
	 */
	IDelayedTaskListener getTaskListener(String taskId);
}
