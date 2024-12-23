package com.yb.yff.game.utils;

import com.alibaba.fastjson.JSON;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import org.springframework.beans.BeanUtils;

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
	 * 创建任务key，
	 * 用于避免重复任务，执行完毕后会移除
	 *
	 * @param requestDTO
	 * @return
	 */
	public static String createTaskKey(GameMessageEnhancedReqDTO requestDTO) {
		GameMessageEnhancedReqDTO keyObj = new GameMessageEnhancedReqDTO();
		BeanUtils.copyProperties(requestDTO, keyObj);
		// 由于客户端每次的请求任务，seq会自增，故需要移除seq，方可重避免复任务
		keyObj.setSeq(null);
		keyObj.setSessionClient2Gate(null);

		return JSON.toJSONString(keyObj);
	}
}
