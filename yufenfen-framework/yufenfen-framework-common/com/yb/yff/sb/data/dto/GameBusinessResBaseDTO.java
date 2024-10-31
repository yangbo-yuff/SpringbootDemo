package com.yb.yff.sb.data.dto;

import com.yb.yff.sb.taskCallback.DelayedExecuteTask;
import lombok.Data;

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
 * @Class: ArmyDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 军队DTO
 */
@Data
public class GameBusinessResBaseDTO {
	/**
	 * 服务端效应编码，0-成功，其它-异常
	 */
	private Integer code;

	/**
	 * 延迟执行任务
	 */
	private DelayedExecuteTask delayedTask;
}
