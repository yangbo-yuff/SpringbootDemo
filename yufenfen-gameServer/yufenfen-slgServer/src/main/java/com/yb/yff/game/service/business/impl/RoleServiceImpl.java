package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.impl.BuildMgrImpl;
import com.yb.yff.game.business.businessLogic.ICityLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.role.*;
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
 * @Description: 游戏角色服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_ROLE)
public class RoleServiceImpl extends BusinessServiceImpl {

	@Autowired
	private ICityLogic cityLogic;

	@Autowired
	private IRoleLogic roleLogic;

	@Autowired
	private BuildMgrImpl buildMgr;


	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("create", this::doCreateRole);
		businessHandlerMap.put("roleList", this::doRoleList);
		businessHandlerMap.put("enterServer", this::doEnterServer);
		businessHandlerMap.put("myCity", this::doMyCity);
		businessHandlerMap.put("myRoleRes", this::doMyRoleRes);
		businessHandlerMap.put("myRoleBuild", this::doMyRoleBuild);
		businessHandlerMap.put("myProperty", this::doMyProperty);
		businessHandlerMap.put("upPosition", this::doUpPosition);
		businessHandlerMap.put("posTagList", this::doPosTagList);
		businessHandlerMap.put("opPosTag", this::doOpPosTag);
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doEnterServer(GameMessageEnhancedReqDTO reqDTO) {
		EnterServerResDTO enterServerResDTO = new EnterServerResDTO();

		EnterServerDTO enterServerDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), EnterServerDTO.class);
		if (enterServerDTO == null) {
			enterServerResDTO.setCode(NetResponseCodeConstants.SessionInvalid.getCode());
			return enterServerResDTO;
		}

		/***************************************************/
		/**********        处理角色登陆逻辑          **********/
		/***************************************************/
		String sessionStr = enterServerDTO.getSession();
		ResponseCode enterServerResult = roleLogic.roleEnterServer(sessionStr, enterServerResDTO);

		if (enterServerResult != NetResponseCodeConstants.SUCCESS) {
			enterServerResDTO.setCode(enterServerResult.getCode());
			return enterServerResDTO;
		}

		/***************************************************/
		/**********        check the city         **********/
		/***************************************************/
		cityLogic.createCitySafely(enterServerResDTO.getRole().getRid());

		// token & time
		enterServerResDTO.setToken(sessionStr);
		enterServerResDTO.setTime(System.currentTimeMillis());

		enterServerResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return enterServerResDTO;
	}

	/**
	 * 创建角色
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doCreateRole(GameMessageEnhancedReqDTO reqDTO) {
		RoleResDTO roleResDTO = new RoleResDTO();

		CreateDTO createDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), CreateDTO.class);

		roleLogic.createRole(createDTO, roleResDTO);

		return roleResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyProperty(GameMessageEnhancedReqDTO reqDTO) {
		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			myPropertyResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return myPropertyResDTO;
		}

		ResponseCode rolePropertyResult = roleLogic.integrateRoleProperty(rid, myPropertyResDTO);

		myPropertyResDTO.setCode(rolePropertyResult.getCode());

		return myPropertyResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doPosTagList(GameMessageEnhancedReqDTO reqDTO) {
		PosTagListResDTO posTagListResDTO = new PosTagListResDTO();
		if (reqDTO.getRid() == null) {
			posTagListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return posTagListResDTO;
		}

		posTagListResDTO.setPosTags(roleLogic.getRolePosTags(reqDTO.getRid()));

		posTagListResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		return posTagListResDTO;
	}

	/**
	 * 更新角色所在地图的中心位置
	 *
	 * @param reqDTO
	 * @return
	 */
	public synchronized GameBusinessResBaseDTO doUpPosition(GameMessageEnhancedReqDTO reqDTO) {
		UpPositionResDTO upPositionResDTO = new UpPositionResDTO();
		upPositionResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			upPositionResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return upPositionResDTO;
		}

		PositionDTO newPosition = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), PositionDTO.class);

		roleLogic.upRoleCenterPos(rid, newPosition);

		return upPositionResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doRoleList(GameMessageEnhancedReqDTO reqDTO) {
		RoleListResDTO roleListResDTO = new RoleListResDTO();

		Integer uid = reqDTO.getUid();
		if (uid == null) {
			roleListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return roleListResDTO;
		}

		List<RoleDTO> roles = roleLogic.getUserRole(uid);

		roleListResDTO.setRoles(roles);
		roleListResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return roleListResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyCity(GameMessageEnhancedReqDTO reqDTO) {
		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			myPropertyResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return myPropertyResDTO;
		}

		// 主城信息
		List<CityDTO> citys = roleLogic.getRoleCitys(rid);
		myPropertyResDTO.setCitys(citys);

		myPropertyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return myPropertyResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyRoleRes(GameMessageEnhancedReqDTO reqDTO) {
		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			myPropertyResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return myPropertyResDTO;
		}

		// 资源
		RoleResourceData roleResourceDTO = roleLogic.getRoleResource(rid);
		myPropertyResDTO.setRoleRes(roleResourceDTO);

		myPropertyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return myPropertyResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyRoleBuild(GameMessageEnhancedReqDTO reqDTO) {
		PosTagListResDTO posTagListResDTO = new PosTagListResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return posTagListResDTO;
		}

		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();

		// 资源
		myPropertyResDTO.setMrBuilds(buildMgr.getRoleBuildDTOs(rid));

		myPropertyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return myPropertyResDTO;
	}

	/**
	 * 添加/移除 标签
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doOpPosTag(GameMessageEnhancedReqDTO reqDTO) {
		PosTagTypeResDTO posTagTypeResDTO = new PosTagTypeResDTO();

		PosTagTypeDTO posTagTypeDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), PosTagTypeDTO.class);

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagTypeResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return posTagTypeResDTO;
		}

		ResponseCode handleResult = roleLogic.handlePosTag(rid, posTagTypeDTO);
		if (handleResult != NetResponseCodeConstants.SUCCESS) {
			posTagTypeResDTO.setCode(handleResult.getCode());
			return posTagTypeResDTO;
		}

		BeanUtils.copyProperties(posTagTypeDTO, posTagTypeResDTO);
		posTagTypeResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return posTagTypeResDTO;
	}
}
