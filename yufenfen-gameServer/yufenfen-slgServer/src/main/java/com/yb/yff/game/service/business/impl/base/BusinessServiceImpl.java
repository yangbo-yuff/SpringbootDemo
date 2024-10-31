package com.yb.yff.game.service.business.impl.base;

import com.yb.yff.game.service.business.IBusinessService;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
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
 * @Description: 游戏业务服务
 */
public abstract class BusinessServiceImpl implements IBusinessService {
	/**
	 * 初始化 业务处理器的 配置器
	 *
	 * @param businessHandlerMap
	 */
	public abstract void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap);

	private Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap = new HashMap<>();

	@PostConstruct
	private void init() {
		initBusinessHandlerMap(businessHandlerMap);
	}

	/**
	 * 分发业务
	 *
	 * @param businessName
	 * @param requestDTO
	 */
	@Override
	public GameMessageEnhancedResDTO dispathBusiness(String businessName, GameMessageEnhancedReqDTO requestDTO) {

		GameMessageEnhancedResDTO resDTO = new GameMessageEnhancedResDTO();

		GameBusinessResBaseDTO gameBusinessResDTO = businessHandlerMap.get(businessName).apply(requestDTO);
		BeanUtils.copyProperties(requestDTO, resDTO);
		resDTO.setCode(gameBusinessResDTO.getCode());
		resDTO.setDelayedTask(gameBusinessResDTO.getDelayedTask());
		gameBusinessResDTO.setCode(null);
		resDTO.setMsg(gameBusinessResDTO);

		return resDTO;
	}
}
