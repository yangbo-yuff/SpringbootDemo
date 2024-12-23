package com.yb.yff.game.service.DelayedTask.impl;

import com.yb.yff.sb.taskCallback.TimeConsumingTaskParam;
import com.yb.yff.game.service.DelayedTask.IDelayedTaskListener;
import com.yb.yff.game.service.DelayedTask.IDelayedTaskService;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

@Service
public class RedisDelayedTaskServiceImpl implements IDelayedTaskService{
	public static final String TASK_KEY = "delayed_tasks_key";
	public static final String TASK_INFO = "delayed_tasks_2clientData";
	public static final String TASK_INFO_PARAMS = "delayed_tasks_info_params";

	@Autowired
	private StringRedisTemplate redisTemplate;

	private final Map<String, IDelayedTaskListener> taskListeners = new ConcurrentHashMap();
	private final Map<String, Consumer<TimeConsumingTaskParam>> taskCallbacks = new ConcurrentHashMap();

	private final Map<String, TimeConsumingTaskParam> taskParameters = new ConcurrentHashMap();

	private final Map<String, GameMessageEnhancedResDTO> task2ClientData = new ConcurrentHashMap();

	/**
	 * 添加一个延时任务
	 *
	 * @param task
	 */
	@Override
	public void addDelayedTask(TimeConsumingTask task, IDelayedTaskListener listener) {
		String taskId = task.getTaskId();
		long executionTime = System.currentTimeMillis() + task.getDelay().toMillis();
		redisTemplate.opsForZSet().add(TASK_KEY, taskId, executionTime);

		// 注册带参数的回调
		taskCallbacks.put(taskId, task.getCallback());
		taskParameters.put(taskId, task.getCallbackParam());

		task2ClientData.put(taskId, task.getResDTO());

		// 监听者
		taskListeners.put(taskId, listener);
	}

	/**
	 * 获取一个延时任务回调函数
	 *
	 * @param taskId
	 * @return
	 */
	public Consumer<TimeConsumingTaskParam> getCallback(String taskId) {
		return taskCallbacks.get(taskId);
	}

	/**
	 * 移除一个延时任务回调函数
	 *
	 * @param taskId
	 */
	public void removeCallback(String taskId) {
		taskCallbacks.remove(taskId);
	}

	/**
	 * 处理一个延时任务结果
	 * @param taskId
	 * @param resDTO
	 */
	@Override
	public void processResult(String taskId, GameMessageEnhancedResDTO resDTO) {
		// TODO 自定义的结果处理逻辑，比如将结果存储在数据库或返回给调用者
		IDelayedTaskListener listener = taskListeners.get(taskId);
		String sessionId = resDTO.getSessionClient2Gate();
		resDTO.setDelayedTasks(null);
		listener.onDelayedTaskFinish(sessionId, taskId, resDTO);
	}

	/**
	 * 获取一个延时任务的参数
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public TimeConsumingTaskParam getTaskParameter(String taskId) {
		return taskParameters.get(taskId);
	}

	/**
	 * 获取一个延时任务的完成时返回给客户端的数据
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public GameMessageEnhancedResDTO getTask2ClientData(String taskId) {
		return task2ClientData.get(taskId);
	}

	/**
	 * 获取一个延时任务的监听者
	 *
	 * @param taskId
	 * @return
	 */
	@Override
	public IDelayedTaskListener getTaskListener(String taskId) {
		return taskListeners.get(taskId);
	}
}
