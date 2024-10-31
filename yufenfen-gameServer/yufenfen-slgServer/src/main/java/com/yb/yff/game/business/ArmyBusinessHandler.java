package com.yb.yff.game.business;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.army.*;
import com.yb.yff.sb.data.dto.role.RoleResourceData;
import org.springframework.stereotype.Component;

import java.util.List;

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
 * @Description: 游戏军队业务处理
 */
@Component
public class ArmyBusinessHandler {

	/**
	 * 军队列表
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyList(GameMessageEnhancedReqDTO reqDTO) {
		MyListDTO myListDTO = (MyListDTO) reqDTO.getMsg();
		Integer cityId = myListDTO.getCityId();

		// TODO 获得列表
		List<ArmyDTO> armys = null;

		MyListResDTO myListResDTO = new MyListResDTO();

		myListResDTO.setCityId(cityId);
		myListResDTO.setArmys(armys);

		return myListResDTO;
	}

	/**
	 * 军队
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyOne(GameMessageEnhancedReqDTO reqDTO) {
		MyOneDTO myOneDTO = (MyOneDTO) reqDTO.getMsg();

		// TODO 根据 myOneDTO 获得军队
		ArmyDTO army = new ArmyDTO();

		return army;
	}


	/**
	 * 军队部署
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDispose(GameMessageEnhancedReqDTO reqDTO) {

		DisposeDTO disposeDTO = (DisposeDTO) reqDTO.getMsg();

		// TODO 根据 disposeDTO 获得 信息
		ArmyDTO army = new ArmyDTO();

		return army;
	}


	/**
	 * 执行征兵
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConscript(GameMessageEnhancedReqDTO reqDTO) {
		ConscriptDTO conscriptDTO = (ConscriptDTO) reqDTO.getMsg();

		// TODO 根据 conscriptDTO 获得列表
		ArmyDTO army = new ArmyDTO();
		RoleResourceData roleRes = new RoleResourceData();

		ConscriptResDTO conscriptResDTO = new ConscriptResDTO();

		conscriptResDTO.setArmy(army);
		conscriptResDTO.setRole_res(roleRes);

		return conscriptResDTO;
	}


	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doAssign(GameMessageEnhancedReqDTO reqDTO) {
		return null;
	}
}
