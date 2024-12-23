package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessLogic.IWarReportLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.constant.StaticConf;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.game.data.dto.war.ReadDTO;
import com.yb.yff.game.data.dto.war.ReadResDTO;
import com.yb.yff.game.data.dto.war.ReportResDTO;
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
 * @Description: 游戏战争业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_WAR)
public class WarServiceImpl extends BusinessServiceImpl {
	@Autowired
	IWarReportLogic warReportLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("report", this::doReport);
		businessHandlerMap.put("read", this::doRead);
	}


	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doReport(GameMessageEnhancedReqDTO reqDTO) {
		ReportResDTO reportResDTO = new ReportResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			reportResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return reportResDTO;
		}

		LogicTaskResultDTO<List<WarReportDTO>> result =  warReportLogic.getLastWarReport(rid, StaticConf.WAR_REPORT_LIMIT);

		if(result.getCode() != NetResponseCodeConstants.SUCCESS){
			reportResDTO.setCode(result.getCode().getCode());
			return reportResDTO;
		}

		reportResDTO.setList(result.getResult());

		reportResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		return reportResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doRead(GameMessageEnhancedReqDTO reqDTO) {
		ReadResDTO readResDTO = new ReadResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			readResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return readResDTO;
		}

		ReadDTO readDTO = JSONObject.toJavaObject((JSONObject)reqDTO.getMsg(), ReadDTO.class);

		ResponseCode result = warReportLogic.readWarReport(rid, readDTO.getId());

		if(result != NetResponseCodeConstants.SUCCESS){
			readResDTO.setCode(result.getCode());
			return readResDTO;
		}

		readResDTO.setId(readDTO.getId());
		readResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		return readResDTO;
	}
}
