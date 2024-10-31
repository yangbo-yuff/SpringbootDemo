package com.yb.yff.game.business;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.general.*;
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
 * @Description: 游戏一般业务处理
 */
@Component
public class GeneralBusinessHandler {

	/**
	 * 武将列表
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyGenerals(GameMessageEnhancedReqDTO reqDTO) {

		// TODO 根据 reqDTO 获得武将信息
		GeneralsResDTO generalsResDTO = new GeneralsResDTO();

		return generalsResDTO;
	}

	/**
	 * 武将抽卡
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDrawGeneral(GameMessageEnhancedReqDTO reqDTO) {
		DrawGeneralDTO drawGeneralDTO = (DrawGeneralDTO)reqDTO.getMsg();

		// TODO 根据 drawGeneralDTO 获得武将信息
		GeneralsResDTO generalsResDTO = new GeneralsResDTO();

		return generalsResDTO;
	}

	/**
	 * 武将流放
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConvert(GameMessageEnhancedReqDTO reqDTO) {
		ConvertDTO convertDTO = (ConvertDTO)reqDTO.getMsg();

		// TODO 根据 convertDTO 获得武将信息
		ConvertResDTO convertResDTO = new ConvertResDTO();

		return convertResDTO;
	}

	/**
	 * 武将合成
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doComposeGeneral(GameMessageEnhancedReqDTO reqDTO) {
		ComposeGeneralDTO convertDTO = (ComposeGeneralDTO)reqDTO.getMsg();

		// TODO 根据 convertDTO 获得武将信息
		GeneralsResDTO generalsResDTO = new GeneralsResDTO();


		return generalsResDTO;
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
