package com.yb.yff.game.service.DelayedTask;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
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
 * @Class: DelayedTaskProcessor
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: 延时任务处理器
 */

@Component
public class DelayedTaskProcessor {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private IDelayedTaskService taskService;

	@Scheduled(fixedRate = 1000)  // 每秒检查一次
	public void processDelayedTasks() {
		long now = System.currentTimeMillis();
		Set<String> tasks = redisTemplate.opsForZSet().rangeByScore("delayed_tasks", 0, now);

		if (tasks != null) {
			for (String taskId : tasks) {
				// 执行并处理延时任务
				handleTask(taskId);

				// 从ZSet中移除已处理的任务
				redisTemplate.opsForZSet().remove("delayed_tasks", taskId);
			}
		}
	}

	private void handleTask(String taskId) {
		System.out.println("Executing task: " + taskId);

		// 获取任务回调
		Function<GameMessageEnhancedResDTO, GameMessageEnhancedResDTO> callback = taskService.getCallback(taskId);
		if (callback != null) {
			// 模拟传入参数的获取
			GameMessageEnhancedResDTO input = (GameMessageEnhancedResDTO) taskService.getTaskParameter(taskId);

			// 执行回调并获得返回值
			GameMessageEnhancedResDTO result = callback.apply(input);

			// 更新处理后的数据或执行后续逻辑
			taskService.processResult(taskId, result);  // 自定义结果处理逻辑

			taskService.removeCallback(taskId);
		}
	}
}

