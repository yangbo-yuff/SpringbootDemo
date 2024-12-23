package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.impl.UnionMgrImpl;
import com.yb.yff.game.business.businessLogic.IUnionLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.union.*;
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
 * @Description: 游戏联盟业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_UNION)
public class UnionServiceImpl extends BusinessServiceImpl {
	@Autowired
	IUnionLogic unionLogic;

	@Autowired
	UnionMgrImpl unionMgr;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("create", this::doCreate);
		businessHandlerMap.put("list", this::doList);
		businessHandlerMap.put("join", this::doJoin);
		businessHandlerMap.put("verify", this::doVerify);
		businessHandlerMap.put("member", this::doMember);
		businessHandlerMap.put("applyList", this::doApplyList);
		businessHandlerMap.put("exit", this::doBExit);
		businessHandlerMap.put("dismiss", this::doDismiss);
		businessHandlerMap.put("notice", this::doNotice);
		businessHandlerMap.put("modNotice", this::doModNotice);
		businessHandlerMap.put("kick", this::doKick);
		businessHandlerMap.put("appoint", this::doAppoint);
		businessHandlerMap.put("abdicate", this::doAbdicate);
		businessHandlerMap.put("info", this::doInfo);
		businessHandlerMap.put("log", this::doLog);
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doCreate(GameMessageEnhancedReqDTO reqDTO) {
		CreateResDTO createResDTO = new CreateResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			createResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return createResDTO;
		}

		CreateDTO createDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), CreateDTO.class);

		LogicTaskResultDTO<UnionDTO> result = unionLogic.createUnion(rid, createDTO.getName());
		if(result.getCode() != NetResponseCodeConstants.SUCCESS){
			createResDTO.setCode(result.getCode().getCode());
			return createResDTO;
		}

		UnionDTO unionDTO = result.getResult();

		createResDTO.setName(unionDTO.getName());
		createResDTO.setId(unionDTO.getId());

		createResDTO.setCode(result.getCode().getCode());

		return createResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doJoin(GameMessageEnhancedReqDTO reqDTO) {
		GameBusinessResBaseDTO joinResult = new GameBusinessResBaseDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			joinResult.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return joinResult;
		}

		InfoDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), InfoDTO.class);

		LogicTaskResultDTO<ResponseCode> resultDTO = unionLogic.joinUnion(rid, infoDTO.getId());

		joinResult.setCode(resultDTO.getCode().getCode());

		return joinResult;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMember(GameMessageEnhancedReqDTO reqDTO) {
		MemberRspDTO memberRsp = new MemberRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			memberRsp.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return memberRsp;
		}

		InfoDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), InfoDTO.class);
		memberRsp.setId(infoDTO.getId());

		LogicTaskResultDTO<List<MemberDTO>> result = unionLogic.getUnionMembers(infoDTO.getId());

		memberRsp.setId(infoDTO.getId());
		memberRsp.setMembers(result.getResult());
		memberRsp.setCode(result.getCode().getCode());

		return memberRsp;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doApplyList(GameMessageEnhancedReqDTO reqDTO) {
		ApplyRspDTO applyRsp = new ApplyRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			applyRsp.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return applyRsp;
		}

		InfoDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), InfoDTO.class);

		List<UnionApplyDTO> applys = unionMgr.getUnionApplyList(infoDTO.getId());

		applyRsp.setId(infoDTO.getId());
		applyRsp.setApplys(applys);
		applyRsp.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return applyRsp;
	}

	/**
	 * 解散
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDismiss(GameMessageEnhancedReqDTO reqDTO) {
		GameBusinessResBaseDTO result = new ApplyRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		unionLogic.dismissUnion(rid);

		result.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return result;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doVerify(GameMessageEnhancedReqDTO reqDTO) {
		VerifyResDTO verifyRes = new VerifyResDTO();
		BeanUtils.copyProperties(reqDTO, verifyRes);

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			verifyRes.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return verifyRes;
		}

		VerifyReqDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), VerifyReqDTO.class);

		ResponseCode code = unionLogic.verifyApplicant(rid, infoDTO);
		verifyRes.setCode(code.getCode());

		return verifyRes;
	}

	public GameBusinessResBaseDTO doBExit(GameMessageEnhancedReqDTO reqDTO) {
		GameBusinessResBaseDTO result = new GameBusinessResBaseDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		ResponseCode exited = unionLogic.exitUnion(rid);

		result.setCode(exited.getCode());

		return result;
	}

	/**
	 * 踢人
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doKick(GameMessageEnhancedReqDTO reqDTO) {
		KickResDTO result = new KickResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		KickReqDTO kickReq  = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), KickReqDTO.class);

		ResponseCode kicked = unionLogic.kickUnion(rid, kickReq.getRid());

		result.setCode(kicked.getCode());

		return result;
	}

	/**
	 * 任命
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doAppoint(GameMessageEnhancedReqDTO reqDTO) {
		AppointResDTO result = new AppointResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		AppointReqDTO appointInfo  = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), AppointReqDTO.class);

		ResponseCode appointed = unionLogic.appointUnion(rid, appointInfo);
		if(appointed.getCode().equals(NetResponseCodeConstants.SUCCESS)){
			BeanUtils.copyProperties(appointInfo, result);
		}

		result.setCode(appointed.getCode());

		return result;
	}

	/**
	 * 禅让
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doAbdicate(GameMessageEnhancedReqDTO reqDTO) {
		GameBusinessResBaseDTO result = new GameBusinessResBaseDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		AbdicateReqDTO taget  = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), AbdicateReqDTO.class);

		ResponseCode abdicated = unionLogic.abdicateUnion(rid, taget.getRid());

		result.setCode(abdicated.getCode());

		return result;
	}

	/**
	 * 修改公告
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doModNotice(GameMessageEnhancedReqDTO reqDTO) {
		ModNoticeRspDTO result = new ModNoticeRspDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			result.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return result;
		}

		ModNoticeReqDTO modNoticeReq  = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ModNoticeReqDTO.class);

		ResponseCode abdicated = unionLogic.modNoticeUnion(rid, modNoticeReq.getText());

		result.setCode(abdicated.getCode());
		result.setText(modNoticeReq.getText());

		return result;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doList(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得联盟信息
		ListResDTO listResDTO = new ListResDTO();

		List<UnionResDTO> unionlist = unionLogic.getUnionResList();

		listResDTO.setList(unionlist);
		listResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return listResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doInfo(GameMessageEnhancedReqDTO reqDTO) {

		InfoResDTO infoResDTO = new InfoResDTO();

		InfoDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), InfoDTO.class);

		LogicTaskResultDTO<UnionResDTO> result = unionLogic.getUnionInfo(infoDTO.getId());
		infoResDTO.setId(infoDTO.getId());

		infoResDTO.setCode(result.getCode().getCode());
		infoResDTO.setInfo(result.getResult());

		return infoResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doLog(GameMessageEnhancedReqDTO reqDTO) {
		LogResDTO logResDTO = new LogResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			logResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return logResDTO;
		}

		LogicTaskResultDTO<List<UnionLogDTO>>  resultDTO = unionLogic.getUnionLogList(rid);
		logResDTO.setLogs(resultDTO.getResult());
		logResDTO.setCode(resultDTO.getCode().getCode());

		return logResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doNotice(GameMessageEnhancedReqDTO reqDTO) {
		NoticeRspDTO result = new NoticeRspDTO();

		InfoDTO infoDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), InfoDTO.class);

		UnionDTO union = unionMgr.getRoleUnion(infoDTO.getId());
		if (union == null) {
			result.setCode(NetResponseCodeConstants.UnionNotFound.getCode());
			return result;
		}

		result.setText(union.getNotice());
		result.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return result;

	}
}
