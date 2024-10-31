package com.yb.yff.game.business;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.war.ReadDTO;
import com.yb.yff.sb.data.dto.war.ReadResDTO;
import com.yb.yff.sb.data.dto.war.ReportResDTO;
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
 * @Description: 游戏战争业务处理
 */
@Component
public class WarBusinessHandler {

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doReport(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得 战役报告 列表信息
		ReportResDTO reportResDTO = new ReportResDTO();
		return reportResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doRead(GameMessageEnhancedReqDTO reqDTO) {
		ReadDTO readDTO = (ReadDTO) reqDTO.getMsg();

		// TODO 根据 reqDTO 获得 战役报告 列表信息
		ReadResDTO readResDTO = new ReadResDTO();
		return readResDTO;
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
