package com.yb.yff.game.service.business.impl;

import com.yb.yff.game.business.businessLogic.ISkillLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.skill.SkillDTO;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.game.data.dto.skill.ListResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
 * @Description: 游戏技能业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_SKILL)
public class SkillServiceImpl extends BusinessServiceImpl {
	@Autowired
	ISkillLogic skillLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("list", this::doList);
	}


	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doList(GameMessageEnhancedReqDTO reqDTO) {
		ListResDTO listResDTO = new ListResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			listResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return listResDTO;
		}

		LogicTaskResultDTO<List<SkillDTO>> result = skillLogic.getSkills(rid);

		listResDTO.setList(result.getResult());

		listResDTO.setCode(result.getCode().getCode());

		return listResDTO;
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
