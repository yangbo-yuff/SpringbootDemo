package com.yb.yff.sb.taskCallback;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import lombok.Data;

import java.time.Duration;
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
 * @Class: TimeConsumingTask
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: 延时执行回调
 */
@Data
public class TimeConsumingTask<T> {
	// 任务ID
	private String taskId;
	// 延时
	private Duration delay;
	// 会话id
	private String sessionId;
	// 响应客户端数据
	private GameMessageEnhancedResDTO resDTO;
	// 回调参数
	private TimeConsumingTaskParam callbackParam;
	// 回调方法
	private Consumer<TimeConsumingTaskParam> callback;
	// 附带信息
	private T appendant;

	public TimeConsumingTask(String taskId, Duration delay, String sessionId,
	                         GameMessageEnhancedResDTO resDTO, T appendant,
	                         TimeConsumingTaskParam callbackParam, Consumer<TimeConsumingTaskParam> callback) {
		this.appendant = appendant;
		this.taskId = taskId;
		this.delay = delay;
		this.sessionId = sessionId;
		this.resDTO = resDTO;
		this.callbackParam = callbackParam;
		this.callback = callback;
	}
}
