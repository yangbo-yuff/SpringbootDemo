package com.yb.yff.game.utils;

import com.alibaba.fastjson.JSON;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;

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
 * @Class: TaskUitls
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: 任务处理相关工具
 */
public class TaskUitls {


	/**
	 * 创建任务key
	 *
	 * @param requestDTO
	 * @return
	 */
	public static String createTaskKey(GameMessageEnhancedReqDTO requestDTO) {
		return JSON.toJSONString(requestDTO);
	}
}
