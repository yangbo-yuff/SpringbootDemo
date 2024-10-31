package com.yb.yff.game.business;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.interior.CollectResDTO;
import com.yb.yff.sb.data.dto.interior.OpenCollectResDTO;
import org.springframework.stereotype.Component;

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
 * @Class: role
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏内部业务处理
 */
@Component
public class InteriorBusinessHandler {

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doOpenCollect(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得用户信息
		OpenCollectResDTO openCollectResDTO = new OpenCollectResDTO();

		return openCollectResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doCollect(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得用户信息
		CollectResDTO collectResDTO = new CollectResDTO();

		return collectResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doBusiness(GameMessageEnhancedReqDTO reqDTO) {
		return null;
	}


}
