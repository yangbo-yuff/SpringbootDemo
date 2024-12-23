package com.yb.yff.sb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

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
 * @Class: AsyncThreadPoolConfig
 * @CreatedOn 2024/12/9.
 * @Email: yangboyff@gmail.com
 * @Description: 线程池配置
 */
@Configuration
public class AsyncThreadPoolConfig {
	public static final String ASYNC_TASK_EXECUTOR_NAME = "asyncTaskExecutor";

	private static final int MAX_POOL_SIZE = 20;

	private static final int CORE_POOL_SIZE = 5;

	private static final int QUEUE_CAPACITY = 50;

	@Bean(ASYNC_TASK_EXECUTOR_NAME)
	public AsyncTaskExecutor asyncTaskExecutor() {
		ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
		asyncTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);  // 核心线程数
		asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);    // 最大线程数
		asyncTaskExecutor.setQueueCapacity(QUEUE_CAPACITY); // 等待队列容量
		asyncTaskExecutor.setThreadNamePrefix("bis-async-task-thread-pool-");
		//拒绝策略：队列已满时，使用调用者线程来执行任务
		asyncTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		asyncTaskExecutor.initialize();
		return asyncTaskExecutor;
	}
}
