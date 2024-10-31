package com.yb.yff.sb.taskCallback;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import lombok.Data;

import java.time.Duration;
import java.util.function.Function;

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
 * @Class: DelayedExecuteTask
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: 延时执行回调
 */
@Data
public class DelayedExecuteTask {
	private String taskId;
	private Duration delay;
	private String sessionId;
	private Function<GameMessageEnhancedResDTO, GameMessageEnhancedResDTO> callback;

    public DelayedExecuteTask(String taskId, Duration delay, String sessionId, Function<GameMessageEnhancedResDTO, GameMessageEnhancedResDTO> callback) {
		this.taskId = taskId;
		this.delay = delay;
		this.sessionId = sessionId;
		this.callback = callback;
	}
}
