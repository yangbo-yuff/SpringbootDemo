package com.yb.yff.game.service.business.impl;

import com.yb.yff.game.business.NationMapBusinessHandler;
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
 * @Description: 游戏游戏国家地图业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_NATIONMAP)
public class NationMapServiceImpl extends BusinessServiceImpl {
	@Autowired
	NationMapBusinessHandler businessHandler;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("config", businessHandler::doConfig);
		businessHandlerMap.put("scanBlock", businessHandler::doScanBlock);
		businessHandlerMap.put("giveUp", businessHandler::doBusiness);
		businessHandlerMap.put("build", businessHandler::doBusiness);
		businessHandlerMap.put("upBuild", businessHandler::doBusiness);
		businessHandlerMap.put("delBuild", businessHandler::doBusiness);
	}
}
