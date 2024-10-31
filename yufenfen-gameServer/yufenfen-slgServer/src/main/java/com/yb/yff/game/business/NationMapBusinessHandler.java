package com.yb.yff.game.business;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.nationMap.ConfigResDTO;
import com.yb.yff.sb.data.dto.nationMap.ScanBlockDTO;
import com.yb.yff.sb.data.dto.nationMap.ScanBlockResDTO;
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
 * @Description: 游戏国家地图业务处理
 */
@Component
public class NationMapBusinessHandler {

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConfig(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得 配置 信息
		ConfigResDTO configResDTO = new ConfigResDTO();


		return configResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doScanBlock(GameMessageEnhancedReqDTO reqDTO) {
		ScanBlockDTO scanBlockDTO = (ScanBlockDTO) reqDTO.getMsg();

		// TODO 根据 ScanBlockDTO 获得 配置 信息
		ScanBlockResDTO scanBlockResDTO = new ScanBlockResDTO();

		return scanBlockResDTO;
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
