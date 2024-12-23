package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessLogic.IArmyLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.*;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
 * @Description: 游戏军队业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_ARMY)
public class ArmyServiceImpl extends BusinessServiceImpl {

	@Autowired
	IArmyLogic armyLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("myList", this::doMyList);
		businessHandlerMap.put("myOne", this::doMyOne);
		businessHandlerMap.put("dispose", this::doDispose);
		businessHandlerMap.put("conscript", this::doConscript);
		businessHandlerMap.put("assign", this::doAssign);
	}

	/**
	 * 军队列表
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyList(GameMessageEnhancedReqDTO reqDTO) {
		MyListResDTO myListResDTO = new MyListResDTO();

		MyListDTO myListDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), MyListDTO.class);

		if (myListDTO == null || myListDTO.getCityId() == null) {
			myListResDTO.setCode(NetResponseCodeConstants.CityNotExist.getCode());
			return myListResDTO;
		}

		Integer cityId = myListDTO.getCityId();

		List<ArmyDTO> armys = new ArrayList<>();

		armyLogic.getCityArmy(cityId).forEach(warArmy -> {
			armys.add(warArmy.getArmy());
		});

		myListResDTO.setCityId(cityId);
		myListResDTO.setArmys(armys);

		myListResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return myListResDTO;
	}

	/**
	 * 获取指定军队
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyOne(GameMessageEnhancedReqDTO reqDTO) {
		ArmyResDTO armyResDTO = new ArmyResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			armyResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return armyResDTO;
		}

		MyOneDTO myOneDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), MyOneDTO.class);

		WarArmyDTO army = armyLogic.getOrderArmy(rid, myOneDTO.getCityId(), myOneDTO.getOrder());

		armyResDTO.setArmy(army.getArmy());
		armyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return armyResDTO;
	}


	/**
	 * 配置将领
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDispose(GameMessageEnhancedReqDTO reqDTO) {
		ArmyResDTO armyResDTO = new ArmyResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			armyResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return armyResDTO;
		}

		DisposeDTO disposeDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), DisposeDTO.class);

		LogicTaskResultDTO<WarArmyDTO> result = armyLogic.configureGenerals(rid, disposeDTO);
		if (result.getCode() != NetResponseCodeConstants.SUCCESS) {
			armyResDTO.setCode(result.getCode().getCode());
			return armyResDTO;
		}

		armyResDTO.setArmy(result.getResult().getArmy());

		armyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return armyResDTO;
	}


	/**
	 * 征兵
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConscript(GameMessageEnhancedReqDTO reqDTO) {
		ConscriptResDTO conscriptResDTO = new ConscriptResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			conscriptResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return conscriptResDTO;
		}

		ConscriptDTO conscriptDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ConscriptDTO.class);

		if (conscriptDTO.getArmyId() <= 0 || conscriptDTO.getCnts().size() != 3 || conscriptDTO.getCnts().get(0) < 0
				|| conscriptDTO.getCnts().get(1) < 0 || conscriptDTO.getCnts().get(2) < 0) {
			conscriptResDTO.setCode(NetResponseCodeConstants.InvalidParam.getCode());
			return conscriptResDTO;
		}

		// 征兵耗时任务
		LogicTaskResultDTO<List<TimeConsumingTask>> result =
				armyLogic.configureSoldiers(reqDTO, rid, conscriptDTO);

		if (result.getCode() != NetResponseCodeConstants.SUCCESS) {
			conscriptResDTO.setCode(result.getCode().getCode());
			return conscriptResDTO;
		}

		ConscriptResDTO delayedRes = (ConscriptResDTO) result.getResult().get(0).getResDTO().getMsg();

		conscriptResDTO.setArmy(delayedRes.getArmy());
		conscriptResDTO.setRole_res(delayedRes.getRole_res());

		conscriptResDTO.setDelayedTask(result.getResult());

		conscriptResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return conscriptResDTO;
	}


	/**
	 * 派遣
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doAssign(GameMessageEnhancedReqDTO reqDTO) {
		ArmyResDTO armyResDTO = new ArmyResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			armyResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return armyResDTO;
		}

		AssignDTO assignDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), AssignDTO.class);

		LogicTaskResultDTO<WarArmyDTO> result = armyLogic.assignArmy(rid, assignDTO);
		if (result.getCode() != NetResponseCodeConstants.SUCCESS) {
			armyResDTO.setCode(result.getCode().getCode());
			return armyResDTO;
		}

		armyResDTO.setArmy(result.getResult().getArmy());

		armyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return armyResDTO;
	}
}
