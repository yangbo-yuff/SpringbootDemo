package com.yb.yff.game.service.business.impl;

import com.yb.yff.game.business.businessLogic.IInteriorLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.game.data.dto.interior.CollectResDTO;
import com.yb.yff.game.data.dto.interior.OpenCollectResDTO;
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
 * @Description: 内务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_INTERIOR)
public class InteriorServiceImpl extends BusinessServiceImpl {

	@Autowired
	IInteriorLogic interiorLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("collect", this::doCollect);
		businessHandlerMap.put("openCollect", this::doOpenCollect);
		businessHandlerMap.put("transform", this::doBusiness);
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doCollect(GameMessageEnhancedReqDTO reqDTO) {
		CollectResDTO collectResDTO = new CollectResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			collectResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return collectResDTO;
		}

		ResponseCode result = interiorLogic.roleCollect(rid, collectResDTO);

		collectResDTO.setCode(result.getCode());

		return collectResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doOpenCollect(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得用户信息
		OpenCollectResDTO openCollectResDTO = new OpenCollectResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			openCollectResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return openCollectResDTO;
		}

		ResponseCode result = interiorLogic.roleOpenCollect(rid, openCollectResDTO);

		openCollectResDTO.setCode(result.getCode());

		return openCollectResDTO;
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
