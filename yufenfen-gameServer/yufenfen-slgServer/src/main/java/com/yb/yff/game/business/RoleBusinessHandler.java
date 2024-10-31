package com.yb.yff.game.business;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.data.entity.RoleEntity;
import com.yb.yff.game.jsondb.data.dto.Basic;
import com.yb.yff.game.service.IGamePublicDataManager;
import com.yb.yff.game.service.IGameRoleDataManager;
import com.yb.yff.game.utils.SessionUtil;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.SessionDTO;
import com.yb.yff.sb.data.dto.army.ArmyData;
import com.yb.yff.sb.data.dto.city.BuildData;
import com.yb.yff.sb.data.dto.city.CenterPosData;
import com.yb.yff.sb.data.dto.city.CityData;
import com.yb.yff.sb.data.dto.city.PositionDTO;
import com.yb.yff.sb.data.dto.general.GeneralData;
import com.yb.yff.sb.data.dto.role.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
 * @Description: 游戏角色业务处理
 */
@Component
public class RoleBusinessHandler {

	@Autowired
	private IGameRoleDataManager gameRoleDataManager;

	@Autowired
	private CityBusinessHandler cityBusinessHandler;

	@Autowired
	private IGamePublicDataManager gamePublicDataManager;

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doEnterServer(GameMessageEnhancedReqDTO reqDTO) {
		EnterServerResDTO enterServerResDTO = new EnterServerResDTO();
		JSONObject msgObj = (JSONObject)reqDTO.getMsg();
		if(msgObj.size() == 0){
			enterServerResDTO.setCode(NetResponseCodeConstants.SessionInvalid.getCode());
			return enterServerResDTO;
		}
		EnterServerDTO enterServerDTO = msgObj.toJavaObject(EnterServerDTO.class);


		/***************************************************/
		/**********         check session         **********/
		/***************************************************/
		SessionDTO session = new SessionDTO();
		String sessionStr = enterServerDTO.getSession();
		try {
			if (!StringUtils.hasLength(sessionStr)) {
				throw new IllegalArgumentException("sessionStr is empty");
			}

			SessionUtil.str2Session(sessionStr, session);

			if (!SessionUtil.isValid(session)) {
				throw new IllegalArgumentException("session is expired");
			}
		} catch (Exception e) {
			enterServerResDTO.setCode(NetResponseCodeConstants.SessionInvalid.getCode());
			return enterServerResDTO;
		}

		/***************************************************/
		/**********         check role exist      **********/
		/***************************************************/
		RoleDTO roleDTO = gameRoleDataManager.getRoleDataByUserId(session.getId(), RoleDTO.class);
		if (roleDTO == null) {
			enterServerResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return enterServerResDTO;
		}
		enterServerResDTO.setRole(roleDTO);

		/***************************************************/
		/**********      check the resource       **********/
		/***************************************************/
		RoleResourceData roleResourceResDTO = gameRoleDataManager.getRoleData(roleDTO.getRid(), RoleResourceData.class);

		// 写入默认初始值
		if (roleResourceResDTO.getGold() == 0) {
			Basic basic = gameRoleDataManager.getJsonConfig(Basic.class);
			BeanUtils.copyProperties(basic.getRole(), roleResourceResDTO);
			gameRoleDataManager.updateRoleData(roleResourceResDTO, true);
		}
		enterServerResDTO.setRole_res(roleResourceResDTO);

		/***************************************************/
		/**********        check the city         **********/
		/***************************************************/
		cityBusinessHandler.createCitySafely(roleDTO.getRid());

		// token & time
		enterServerResDTO.setToken(sessionStr);
		enterServerResDTO.setTime(System.currentTimeMillis());

		return enterServerResDTO;
	}

	/**
	 * 创建角色
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO createRole(GameMessageEnhancedReqDTO reqDTO) {
		RoleResDTO roleResDTO = new RoleResDTO();

		CreateDTO createDTO = (CreateDTO) reqDTO.getMsg();

		Integer rid = gameRoleDataManager.getRoleId(createDTO.getUid());
		if (rid != null) {
			roleResDTO.setCode(NetResponseCodeConstants.RoleAlreadyCreate.getCode());
			return roleResDTO;
		}

		RoleEntity roleEntity = gameRoleDataManager.createRoleData(createDTO);
		if (roleEntity == null) {
			roleResDTO.setCode(NetResponseCodeConstants.DBError.getCode());
			return roleResDTO;
		}

		BeanUtils.copyProperties(roleEntity, roleResDTO);
		roleResDTO.setRid(roleEntity.getId());
		roleResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return roleResDTO;
	}

	/**
	 * 业务处理
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doMyProperty(GameMessageEnhancedReqDTO reqDTO) {
		PosTagListResDTO posTagListResDTO = new PosTagListResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return posTagListResDTO;
		}

		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();

		// 资源
		RoleResourceData roleResourceDTO = gameRoleDataManager.getRoleData(rid, RoleResourceData.class);
		myPropertyResDTO.setRole_res(roleResourceDTO);

		// 主城信息
		CityData cityData = gameRoleDataManager.getRoleData(rid, CityData.class);
		myPropertyResDTO.setCitys(cityData.getCityList());

		// 建筑
		BuildData buildData = gameRoleDataManager.getRoleData(rid, BuildData.class);
		myPropertyResDTO.setMr_builds(buildData.getBuildList());

		// 军队
		ArmyData armyData = gameRoleDataManager.getRoleData(rid, ArmyData.class);
		myPropertyResDTO.setArmys(armyData.getArmyList());

		// 武将
		GeneralData generalData = gameRoleDataManager.getRoleData(rid, GeneralData.class);
		myPropertyResDTO.setGenerals(generalData.getGeneralDTOList());

		myPropertyResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

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

		RoleAttrDTO roleAttrDTO = gameRoleDataManager.getRoleData(reqDTO.getRid(), RoleAttrDTO.class);

		String posTags = roleAttrDTO.getPosTags();
		if (posTags != null) {
			PosTagDTO[] posTagList = JSONObject.parseObject(posTags, PosTagDTO[].class);

			posTagListResDTO.setPos_tags(List.of(posTagList));
		}

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

		PositionDTO newPosition = (PositionDTO) reqDTO.getMsg();

		// TODO 删除旧中心位置
		CenterPosData centerPosData = gameRoleDataManager.getRoleData(rid, CenterPosData.class);
		PositionDTO oldCenterPos = centerPosData.getCenterPos();
		if(oldCenterPos.getX() == newPosition.getX() &&
				oldCenterPos.getY() == newPosition.getY()){
			return upPositionResDTO;
		}

		List<Integer> roles = gamePublicDataManager.getRolesByPosition(oldCenterPos);
		List<Integer> rmRoles = roles.stream().filter(r -> r == rid).collect(Collectors.toList());
		roles.removeAll(rmRoles);


		// TODO 写入新中心位置
		centerPosData.setCenterPos(newPosition);
		gameRoleDataManager.updateRoleData(centerPosData, false);

		gamePublicDataManager.updatePositionRole(newPosition, roles);

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

		RoleDTO roleData = gameRoleDataManager.getRoleDataByUserId(uid, RoleDTO.class);

		roleListResDTO.setRoles(List.of(roleData));
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
		PosTagListResDTO posTagListResDTO = new PosTagListResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return posTagListResDTO;
		}

		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();

		// 主城信息
		CityData cityData = gameRoleDataManager.getRoleData(rid, CityData.class);
		myPropertyResDTO.setCitys(cityData.getCityList());

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
		PosTagListResDTO posTagListResDTO = new PosTagListResDTO();
		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagListResDTO.setCode(NetResponseCodeConstants.RoleNotInConnect.getCode());
			return posTagListResDTO;
		}

		MyPropertyResDTO myPropertyResDTO = new MyPropertyResDTO();

		// 资源
		RoleResourceData roleResourceDTO = gameRoleDataManager.getRoleData(rid, RoleResourceData.class);
		myPropertyResDTO.setRole_res(roleResourceDTO);

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
		BuildData buildData = gameRoleDataManager.getRoleData(rid, BuildData.class);
		myPropertyResDTO.setMr_builds(buildData.getBuildList());

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

		PosTagTypeDTO posTagTypeDTO = (PosTagTypeDTO) reqDTO.getMsg();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			posTagTypeResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return posTagTypeResDTO;
		}

		switch (posTagTypeDTO.getType()) {
			case 0:
				// 删除标签
				if (!rmPosTag(rid, posTagTypeDTO, posTagTypeResDTO)) {
					return posTagTypeResDTO;
				}
				break;
			case 1:
				// 添加标签
				if (!addPosTag(rid, posTagTypeDTO, posTagTypeResDTO)) {
					return posTagTypeResDTO;
				}
				break;
			default:
				posTagTypeResDTO.setCode(NetResponseCodeConstants.InvalidParam.getCode());
				return posTagTypeResDTO;
		}

		BeanUtils.copyProperties(posTagTypeDTO, posTagTypeResDTO);
		posTagTypeResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return posTagTypeResDTO;
	}


	/**
	 * 删除标签
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 * @param posTagTypeResDTO
	 * @return
	 */
	private boolean rmPosTag(Integer rid, PosTagTypeDTO posTagTypeDTO, PosTagTypeResDTO posTagTypeResDTO) {
		try {
			RoleAttrDTO roleAttrDTO = gameRoleDataManager.getRoleData(rid, RoleAttrDTO.class);
			String posTags = roleAttrDTO.getPosTags();
			PosTagDTO[] posTagDTOs = JSONObject.parseObject(posTags, PosTagDTO[].class);

			// 移除标记
			List<PosTagDTO> posTagList = List.of(posTagDTOs);
			List<PosTagDTO> removeList = posTagList
					.stream()
					.filter(item -> item.getX() == posTagTypeDTO.getX() && item.getY() == posTagTypeDTO.getY())
					.collect(Collectors.toList());
			posTagList.remove(removeList);

			roleAttrDTO.setPosTags(JSONObject.toJSONString(posTagList));

			gameRoleDataManager.updateRoleData(roleAttrDTO, true);
		} catch (Exception e) {
			posTagTypeResDTO.setCode(NetResponseCodeConstants.InvalidParam.getCode());
			return false;
		}
		return true;
	}

	/**
	 * 添加标签
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 * @param posTagTypeResDTO
	 * @return
	 */
	private boolean addPosTag(Integer rid, PosTagTypeDTO posTagTypeDTO, PosTagTypeResDTO posTagTypeResDTO) {
		try {
			Basic basic = gameRoleDataManager.getJsonConfig(Basic.class);
			Integer pos_tag_limit = basic.getRole().getPos_tag_limit();

			RoleAttrDTO roleAttrDTO = gameRoleDataManager.getRoleData(rid, RoleAttrDTO.class);
			String posTags = roleAttrDTO.getPosTags();

			PosTagDTO[] posTagDTOs = JSONObject.parseObject(posTags, PosTagDTO[].class);

			if (posTagDTOs.length >= pos_tag_limit) {
				posTagTypeResDTO.setCode(NetResponseCodeConstants.OutPosTagLimit.getCode());
				return false;
			}

			// 标记
			PosTagDTO posTagDTO = new PosTagDTO();
			BeanUtils.copyProperties(posTagTypeDTO, posTagDTO);
			List<PosTagDTO> posTagList = List.of(posTagDTOs);
			posTagList.add(posTagDTO);
			roleAttrDTO.setPosTags(JSONObject.toJSONString(posTagList));

			gameRoleDataManager.updateRoleData(roleAttrDTO, true);
		} catch (Exception e) {
			posTagTypeResDTO.setCode(NetResponseCodeConstants.InvalidParam.getCode());
			return false;
		}

		return true;
	}
}
