package com.yb.yff.game.data.dto;

import com.yb.yff.sb.constant.ResponseCode;
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
 * @Class: LogicTaskResultDTO
 * @CreatedOn 2024/11/4.
 * @Email: yangboyff@gmail.com
 * @Description: 逻辑层任务处理结果数据
 */
@Data
public class LogicTaskResultDTO<T> {
	private ResponseCode code;
	private T result;

	// 提供无参构造方法
	public LogicTaskResultDTO() {}

	// 提供带参数的构造方法
	public LogicTaskResultDTO(ResponseCode code, T result) {
		this.code = code;
		this.result = result;
	}
}
