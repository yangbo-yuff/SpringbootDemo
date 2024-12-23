package com.yb.yff.game.service.DelayedTask;

import com.yb.yff.game.data.dto.army.ArmyDTO;

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
 * @Class: ITaskExecutionProcessListener
 * @CreatedOn 2024/11/24.
 * @Email: yangboyff@gmail.com
 * @Description: 执行过程监听者，在任务执行过程中，任务相关或其它的，需要做随着时间而变化的状态更新等等的过程变量更新
 */
public interface ITaskExecutionProcessListener {

	/**
	 * 在任务执行过程中，任务相关或其它的，需要做随着时间而变化的状态更新等等的过程变量更新
	 * 在Service层实现
	 */
	void onTaskProcessed();
}
