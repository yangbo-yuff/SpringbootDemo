package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessLogic.ICityLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.game.data.dto.city.CityBaseDTO;
import com.yb.yff.game.data.dto.city.FacilityResDTO;
import com.yb.yff.game.data.dto.city.UpFacilitieResDTO;
import com.yb.yff.game.data.dto.city.UpFacilityDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import org.springframework.beans.BeanUtils;
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
 * @Description: 游戏游城市业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_CITY)
public class CityServiceImpl extends BusinessServiceImpl {
	@Autowired
	ICityLogic cityLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("facilities", this::doFacilities);
		businessHandlerMap.put("upFacility", this::doUpFacilitie);
	}

	/**
	 * 设施列表
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doFacilities(GameMessageEnhancedReqDTO reqDTO) {
		FacilityResDTO facilityResDTO = new FacilityResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			facilityResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return facilityResDTO;
		}
		CityBaseDTO dto = JSONObject.toJavaObject((JSONObject)reqDTO.getMsg(), CityBaseDTO.class);
		Integer cityID = dto.getCityId();

		ResponseCode responseCode = cityLogic.getRoleCityFacilities(facilityResDTO, rid, cityID);

		if (responseCode != NetResponseCodeConstants.SUCCESS) {
			facilityResDTO.setCode(responseCode.getCode());
			return facilityResDTO;
		}

		facilityResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return facilityResDTO;
	}

	/**
	 * 设施 升级
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doUpFacilitie(GameMessageEnhancedReqDTO reqDTO) {
		UpFacilitieResDTO upFacilitieResDTO = new UpFacilitieResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return upFacilitieResDTO;
		}

		UpFacilityDTO upFacilityDTO = JSONObject.toJavaObject((JSONObject)reqDTO.getMsg(), UpFacilityDTO.class);

		// 创建耗时任务
		LogicTaskResultDTO<TimeConsumingTask> createTCTResult = cityLogic.createUpFacilityTCT(reqDTO, rid, upFacilityDTO);

		if (createTCTResult.getCode() != NetResponseCodeConstants.SUCCESS) {
			upFacilitieResDTO.setCode(createTCTResult.getCode().getCode());
			return upFacilitieResDTO;
		}

		UpFacilitieResDTO delayedTaskResDTO = (UpFacilitieResDTO)createTCTResult.getResult().getResDTO().getMsg();
		BeanUtils.copyProperties(delayedTaskResDTO, upFacilitieResDTO);

		// 延时执行
		List<TimeConsumingTask> delayedTasks = new ArrayList<>();
		delayedTasks.add(createTCTResult.getResult());
		upFacilitieResDTO.setDelayedTask(delayedTasks);

		upFacilitieResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return upFacilitieResDTO;
	}
}
