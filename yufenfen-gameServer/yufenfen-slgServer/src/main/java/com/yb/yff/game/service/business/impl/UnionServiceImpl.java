package com.yb.yff.game.service.business.impl;

import com.yb.yff.game.business.UnionBusinessHandler;
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
 * @Description: 游戏联盟业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_UNION)
public class UnionServiceImpl extends BusinessServiceImpl {
	@Autowired
	UnionBusinessHandler businessHandler;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("create", businessHandler::doCreate);
		businessHandlerMap.put("join", businessHandler::doBusiness);
		businessHandlerMap.put("list", businessHandler::doList);
		businessHandlerMap.put("member", businessHandler::doBusiness);
		businessHandlerMap.put("applyList", businessHandler::doBusiness);
		businessHandlerMap.put("dismiss", businessHandler::doBusiness);
		businessHandlerMap.put("verify", businessHandler::doBusiness);
		businessHandlerMap.put("exit", businessHandler::doBusiness);
		businessHandlerMap.put("kick", businessHandler::doBusiness);
		businessHandlerMap.put("appoint", businessHandler::doBusiness);
		businessHandlerMap.put("abdicate", businessHandler::doBusiness);
		businessHandlerMap.put("modNotice", businessHandler::doBusiness);
		businessHandlerMap.put("info", businessHandler::doInfo);
		businessHandlerMap.put("log", businessHandler::doBusiness);
		businessHandlerMap.put("push", businessHandler::doBusiness);
	}
}
