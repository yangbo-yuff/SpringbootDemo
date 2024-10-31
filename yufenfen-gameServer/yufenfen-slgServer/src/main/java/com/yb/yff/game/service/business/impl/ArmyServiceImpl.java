package com.yb.yff.game.service.business.impl;

import com.yb.yff.game.business.ArmyBusinessHandler;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

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
 * @Class: GameRoleServiceImpl
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏军队业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_ARMY)
public class ArmyServiceImpl extends BusinessServiceImpl {
	@Autowired
	ArmyBusinessHandler businessHandler;

//	@Autowired
//	WSEventServiceImpl wsEventService;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("myList", businessHandler::doMyList);
		businessHandlerMap.put("myOne", businessHandler::doMyOne);
		businessHandlerMap.put("dispose", businessHandler::doDispose);
		businessHandlerMap.put("conscript", businessHandler::doConscript);
		businessHandlerMap.put("assign", businessHandler::doAssign);
	}

//	/**
//	 * 额外处理 业务处理的结果
//	 * @param requestDTO
//	 * @param gameBusinessResBaseDTO
//	 */
//	@Override
//	public void handlResult(GameMessageEnhancedReqDTO requestDTO, GameBusinessResBaseDTO gameBusinessResBaseDTO) {
//
//		GameMessageEnhancedResDTO resDTO = new GameMessageEnhancedResDTO();
//		BeanUtils.copyProperties(requestDTO, resDTO);
//		resDTO.setMsg(gameBusinessResBaseDTO);
//		resDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
//
//		wsEventService.sendMessage(resDTO);
//	}
}
