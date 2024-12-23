package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessLogic.IGeneralLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.general.*;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import org.springframework.beans.BeanUtils;
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
 * @Description: 业务:将领
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_GENERAL)
public class GeneralServiceImpl extends BusinessServiceImpl {
	@Autowired
	IGeneralLogic generalLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("myGenerals", this::doMyGenerals);
		businessHandlerMap.put("drawGeneral", this::doDrawGeneral);
		businessHandlerMap.put("composeGeneral", this::doComposeGeneral);
		businessHandlerMap.put("convert", this::doConvert);
		businessHandlerMap.put("addPrGeneral", this::doAddPrGeneral);
		businessHandlerMap.put("upSkill", this::doUpSkill);
		businessHandlerMap.put("downSkill", this::doDownSkill);
		businessHandlerMap.put("lvSkill", this::doLevelSkill);
	}

	/**
	 * 将领列表
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyGenerals(GameMessageEnhancedReqDTO reqDTO) {
		GeneralsResDTO generalsResDTO = new GeneralsResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			generalsResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return generalsResDTO;
		}

		generalsResDTO.setGenerals(generalLogic.getRoleGenerals(rid));

		generalsResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return generalsResDTO;
	}

	/**
	 * 抽将领卡片
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDrawGeneral(GameMessageEnhancedReqDTO reqDTO) {
		GeneralsResDTO generalsResDTO = new GeneralsResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			generalsResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return generalsResDTO;
		}

		DrawGeneralDTO drawGeneralDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), DrawGeneralDTO.class);

		Integer drawTimes = drawGeneralDTO.getDrawTimes();

		List<GeneralDTO> generals = generalLogic.drawGeneral(rid, drawTimes);
		if (generals == null || generals.size() == 0) {
			generalsResDTO.setCode(NetResponseCodeConstants.DBError.getCode());
			return generalsResDTO;
		}

		generalsResDTO.setGenerals(generals);

		generalsResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return generalsResDTO;
	}

	/**
	 * 将领流放
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConvert(GameMessageEnhancedReqDTO reqDTO) {
		ConvertResDTO convertRes = new ConvertResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			convertRes.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return convertRes;
		}

		ConvertDTO convertDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ConvertDTO.class);

		List<Integer> convertIDs = convertDTO.getGIds();

		ResponseCode code = generalLogic.convertGeneral(rid, convertIDs, convertRes);
		if (code != NetResponseCodeConstants.SUCCESS) {
			convertRes.setCode(code.getCode());
			return convertRes;
		}

		convertRes.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return convertRes;
	}

	/**
	 * 将领合成
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doComposeGeneral(GameMessageEnhancedReqDTO reqDTO) {
		ComposeGeneralRspDTO generalsResDTO = new ComposeGeneralRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			generalsResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return generalsResDTO;
		}

		ComposeGeneralDTO composeGeneral = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ComposeGeneralDTO.class);

		LogicTaskResultDTO<List<GeneralDTO>>  result = generalLogic.composeGeneral(rid, composeGeneral);
		generalsResDTO.setGenerals(result.getResult());

		generalsResDTO.setCode(result.getCode().getCode());

		return generalsResDTO;
	}

	/**
	 * 将领
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doAddPrGeneral(GameMessageEnhancedReqDTO reqDTO) {
		AddPrGeneralRspDTO generalsRes = new AddPrGeneralRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			generalsRes.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return generalsRes;
		}

		AddPrGeneralReqDTO composeGeneral = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), AddPrGeneralReqDTO.class);

		LogicTaskResultDTO<GeneralDTO>  result = generalLogic.addPrGeneral(rid, composeGeneral);
		generalsRes.setGeneral(result.getResult());
		generalsRes.setCode(result.getCode().getCode());
		return generalsRes;
	}

	/**
	 * 学习/升级技能
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doUpSkill(GameMessageEnhancedReqDTO reqDTO) {
		UpDownSkillRspDTO killRes = new UpDownSkillRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			killRes.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return killRes;
		}

		UpDownSkillReqDTO killReq = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), UpDownSkillReqDTO.class);
		BeanUtils.copyProperties(killReq, killRes);

		ResponseCode result = generalLogic.upGeneralKill(rid, killReq);

		killRes.setCode(result.getCode());

		return killRes;
	}

	/**
	 * 降级技能
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDownSkill(GameMessageEnhancedReqDTO reqDTO) {
		UpDownSkillRspDTO killRes = new UpDownSkillRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			killRes.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return killRes;
		}

		UpDownSkillReqDTO killReq = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), UpDownSkillReqDTO.class);
		BeanUtils.copyProperties(killReq, killRes);

		ResponseCode result = generalLogic.downGeneralKill(rid, killReq);

		killRes.setCode(result.getCode());

		return killRes;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doLevelSkill(GameMessageEnhancedReqDTO reqDTO) {

		LvSkillRspDTO killRes = new LvSkillRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			killRes.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return killRes;
		}

		LvSkillReqDTO killReq = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), LvSkillReqDTO.class);
		BeanUtils.copyProperties(killReq, killRes);

		ResponseCode result = generalLogic.lvGeneralKill(rid, killReq);

		killRes.setCode(result.getCode());

		return killRes;
	}


}
