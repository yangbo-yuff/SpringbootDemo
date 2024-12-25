package com.yb.yff.game.business.businessLogic.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yb.yff.game.business.businessDataMgr.impl.*;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigLevelNeedDTO;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.army.ArmyDTO;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.role.*;
import com.yb.yff.game.data.entity.RoleEntity;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.SessionDTO;
import com.yb.yff.sb.utils.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 角色业务逻辑
 */

@Service
@Slf4j
public class RoleLogicImpl extends BusinessDataSyncImpl<RoleResourceData> implements IRoleLogic {

	@Autowired
	private RoleDataMgrImpl roleDataMgr;

	@Autowired
	BuildMgrImpl buildMgrImpl;

	@Autowired
	CityMgrImpl cityMgrImpl;

	@Autowired
	ArmyMgrImpl armyMgrImpl;

	@Autowired
	GeneralMgrImpl generalMgrImpl;

	@Autowired
	IPushService pushService;

	@Autowired
	public void init(){
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_roleRes, pushService);
	}

	/**
	 * 按时生产资源，每十秒执行一次
	 */
	@Scheduled(fixedRate = 20000)
	public void produceResourceOnTime() {
		// 按产量生产资源
		roleCollect(1);

		syncExecute(null, null);
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param roleResource
	 */
	@Override
	public void syncExecute(Integer rid, RoleResourceData roleResource) {
		// 广播角色资源到 客户端
		Map<Integer, RoleResourceData> roleResources = roleDataMgr.getRoleResources();
		roleResources.forEach((roleId, roleRes) -> {
			pushData(roleId, roleRes);
		});
	}

	/**
	 * 按资源产量的 倍数采集资源
	 *
	 * @param multiple
	 */
	@Override
	public void roleCollect(Integer multiple) {
		// 按产量生产资源
		roleDataMgr.productResourceByYield(multiple);
	}

	/**
	 * 角色登录
	 *
	 * @param sessionStr
	 * @param enterServerResDTO
	 */
	@Override
	public ResponseCode roleEnterServer(String sessionStr, EnterServerResDTO enterServerResDTO) {
		/***************************************************/
		/**********         check session         **********/
		/***************************************************/
		SessionDTO session = new SessionDTO();
		try {
			if (!StringUtils.hasLength(sessionStr)) {
				throw new IllegalArgumentException("sessionStr is empty");
			}

			SessionUtil.str2Session(sessionStr, session);

			if (!SessionUtil.isValid(session)) {
				throw new IllegalArgumentException("session is expired");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return NetResponseCodeConstants.SessionInvalid;
		}


		/***************************************************/
		/**********         check role exist      **********/
		/***************************************************/
		RoleDTO roleDTO = roleDataMgr.getRoleByUid(session.getId());

		if (roleDTO == null) {
			return NetResponseCodeConstants.RoleNotExist;
		}
		enterServerResDTO.setRole(roleDTO);


		/***************************************************/
		/**********      check the resource       **********/
		/***************************************************/
		RoleResourceData roleResourceResDTO = roleDataMgr.getRoleResourceData(roleDTO.getRid());
		enterServerResDTO.setRoleRes(roleResourceResDTO);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 创建角色
	 *
	 * @param createDTO
	 * @param roleResDTO
	 */
	@Override
	public void createRole(CreateDTO createDTO, RoleResDTO roleResDTO) {

		RoleDTO role = roleDataMgr.getRoleByUid(createDTO.getUid());
		if (role != null) {
			roleResDTO.setCode(NetResponseCodeConstants.RoleAlreadyCreate.getCode());
			return;
		}

		RoleEntity roleEntity = roleDataMgr.createRoleData(createDTO);
		if (roleEntity == null) {
			roleResDTO.setCode(NetResponseCodeConstants.DBError.getCode());
			return;
		}

		BeanUtils.copyProperties(roleEntity, roleResDTO);
		roleResDTO.setRid(roleEntity.getId());
		roleResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
	}

	/**
	 * 整合角色属性
	 *
	 * @param myPropertyResDTO
	 */
	@Override
	public ResponseCode integrateRoleProperty(Integer rid, MyPropertyResDTO myPropertyResDTO) {
		// 资源
		myPropertyResDTO.setRoleRes(roleDataMgr.getRoleResourceData(rid));

		// 主城信息
		myPropertyResDTO.setCitys(cityMgrImpl.getCitys(rid));

		// 建筑
		myPropertyResDTO.setMrBuilds(buildMgrImpl.getRoleBuildDTOs(rid));

		// 军队
		List<ArmyDTO> rmyS = new ArrayList<>();
		for (WarArmyDTO warArmy : armyMgrImpl.getRoleArmy(rid)) {
			rmyS.add(warArmy.getArmy());
		}
		myPropertyResDTO.setArmys(rmyS);

		// 将领
		myPropertyResDTO.setGenerals(generalMgrImpl.getCityGenerals(rid));

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 获取角色标记位置
	 *
	 * @return
	 */
	@Override
	public List<PosTagDTO> getRolePosTags(Integer rid) {
		RoleDTO role = roleDataMgr.getRoleDTO(rid);
		if (role == null) {
			return new ArrayList<>();
		}

		String posTagsStr = role.getPosTags();
		if (posTagsStr == null) {
			return new ArrayList<>();
		}

		return JSON.parseArray(posTagsStr, PosTagDTO.class);
	}


	/**
	 * 更新角色中心位置
	 *
	 * @return
	 */
	@Override
	public void upRoleCenterPos(Integer rid, PositionDTO newPosition) {

		RoleDTO roleDTO = roleDataMgr.getRoleDTO(rid);
		PositionDTO oldCenterPos = roleDTO.getCenterPos();
		if (oldCenterPos == null ||
				(oldCenterPos.getX() == newPosition.getX() && oldCenterPos.getY() == newPosition.getY())) {
			return;
		}

		// 删除旧中心位置
		List<Integer> roles = roleDataMgr.getRolesByPosition(oldCenterPos);
		List<Integer> rmRoles = roles.stream().filter(r -> r == rid).collect(Collectors.toList());
		roles.removeAll(rmRoles);


		// 写入新中心位置
		roleDTO.setCenterPos(newPosition);
		roleDataMgr.updatePositionRole2Cache(newPosition, rid);
	}


	/**
	 * 获取用户角色
	 *
	 * @param uid
	 * @return
	 */
	@Override
	public List<RoleDTO> getUserRole(Integer uid) {
		RoleDTO roleData = roleDataMgr.getRoleByUid(uid);

		return List.of(roleData);
	}

	/**
	 * 获取用户角色
	 *
	 * @param rid
	 * @return
	 */
	public RoleDTO getRole(Integer rid) {
		return roleDataMgr.getRoleDTO(rid);
	}

	/**
	 * 获取角色主城信息
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public List<CityDTO> getRoleCitys(Integer rid) {
		return cityMgrImpl.getCitys(rid);
	}

	/**
	 * 获取角色资源
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public RoleResourceData getRoleResource(Integer rid) {
		return roleDataMgr.getRoleResourceData(rid);
	}

	/**
	 * 处理标记
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 */
	@Override
	public ResponseCode handlePosTag(Integer rid, PosTagTypeDTO posTagTypeDTO) {
		ResponseCode handleResult = NetResponseCodeConstants.SUCCESS;
		switch (posTagTypeDTO.getType()) {
			case 0:
				// 删除标签
				handleResult = rmPosTag(rid, posTagTypeDTO);
				break;
			case 1:
				// 添加标签
				handleResult = addPosTag(rid, posTagTypeDTO);
				break;
			default:
				handleResult = NetResponseCodeConstants.InvalidParam;
		}

		return handleResult;
	}

	/**
	 * 收支 资源
	 *
	 * @param rid
	 * @param cost
	 * @param isIn 是否入账
	 * @return
	 */
	@Override
	public boolean updateRoleResource(Integer rid, RoleResourceDTO cost, boolean isIn) {

		RoleDTO role = roleDataMgr.getRoleDTO(rid);

		if (cost.getWood() != null) {
			Integer newValue = isIn ? role.getWood() + cost.getWood() : role.getWood() - cost.getWood();
			role.setWood(newValue);
			cost.setWood(newValue);
		}

		if (cost.getIron() != null) {
			Integer newValue = isIn ? role.getIron() + cost.getIron() : role.getIron() - cost.getIron();
			role.setIron(newValue);
			cost.setIron(newValue);
		}

		if (cost.getStone() != null) {
			Integer newValue = isIn ? role.getStone() + cost.getStone() : role.getStone() - cost.getStone();
			role.setStone(newValue);
			cost.setStone(newValue);
		}

		if (cost.getGrain() != null) {
			Integer newValue = isIn ? role.getGrain() + cost.getGrain() : role.getGrain() - cost.getGrain();
			role.setGrain(newValue);
			cost.setGrain(newValue);
		}

		if (cost.getGold() != null) {
			Integer newValue = isIn ? role.getGold() + cost.getGold() : role.getGold() - cost.getGold();
			role.setGold(newValue);
			cost.setGold(newValue);
		}

		if (cost.getDecree() != null) {
			Integer newValue = isIn ? role.getDecree() + cost.getDecree() : role.getDecree() - cost.getDecree();
			role.setDecree(newValue);
			role.setDecree(newValue);
		}

		RoleDTO updateRole = new RoleDTO();
		BeanUtils.copyProperties(cost, updateRole);
		roleDataMgr.updateRoleDataToDB(updateRole);
		return true;
	}

	/**
	 * 检查资源是否足够
	 *
	 * @param rid
	 * @param need
	 * @return
	 */
	@Override
	public ResponseCode checkUserNeed(Integer rid, MBCustomConfigLevelNeedDTO need) {

		RoleResourceData rrData = roleDataMgr.getRoleResourceData(rid);

		if (rrData == null) {
			return NetResponseCodeConstants.RoleNotExist;
		}

		if (need.getDecree() <= rrData.getDecree()
				&& need.getGrain() <= rrData.getGrain()
				&& need.getStone() <= rrData.getStone()
				&& need.getWood() <= rrData.getWood()
				&& need.getIron() <= rrData.getIron()
				&& need.getGold() <= rrData.getGold()) {
			rrData.setDecree(rrData.getDecree() - need.getDecree());
			rrData.setIron(rrData.getIron() - need.getIron());
			rrData.setWood(rrData.getWood() - need.getWood());
			rrData.setStone(rrData.getStone() - need.getStone());
			rrData.setGrain(rrData.getGrain() - need.getGrain());
			rrData.setGold(rrData.getGold() - need.getGold());

			// TODO 不知道干啥			rr.SyncExecute()
			return NetResponseCodeConstants.SUCCESS;
		} else {
			if (need.getDecree() > rrData.getDecree()) {
				return NetResponseCodeConstants.DecreeNotEnough;
			} else {
				return NetResponseCodeConstants.ResNotEnough;
			}
		}
	}

	/**
	 * 删除标签
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 * @return
	 */
	private ResponseCode rmPosTag(Integer rid, PosTagTypeDTO posTagTypeDTO) {
		try {
			RoleDTO role = roleDataMgr.getRoleDTO(rid);

			if (role == null || role.getPosTags() == null) {
				return NetResponseCodeConstants.InvalidParam;
			}

			String posTags = role.getPosTags();

			List<PosTagDTO> posTagList = new ArrayList<>();
			posTagList.addAll(JSON.parseArray(posTags, PosTagDTO.class));


			List<PosTagDTO> removeList = posTagList.stream().filter(item -> item.getX() == posTagTypeDTO.getX() && item.getY() == posTagTypeDTO.getY()).collect(Collectors.toList());
			posTagList.remove(removeList);

			String newPosTags = JSONArray.toJSONString(posTagList);
			role.setPosTags(newPosTags);

			// 仅更新 posTags 到数据库
			RoleDTO updateRole = new RoleDTO();
			updateRole.setPosTags(newPosTags);
			updateRole.setRid(rid);
			roleDataMgr.updateRoleDataToDB(updateRole);
		} catch (Exception e) {
			return NetResponseCodeConstants.InvalidParam;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 添加标签
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 * @return
	 */
	private ResponseCode addPosTag(Integer rid, PosTagTypeDTO posTagTypeDTO) {
		try {
			Integer pos_tag_limit = roleDataMgr.getRoleConfig().getPos_tag_limit();

			RoleDTO role = roleDataMgr.getRoleDTO(rid);
			String posTags = role.getPosTags();

			List<PosTagDTO> posTagList = new ArrayList<>();
			if (posTags != null) {
				posTagList.addAll(JSON.parseArray(posTags, PosTagDTO.class));
			}

			if (posTagList.size() >= pos_tag_limit) {
				return NetResponseCodeConstants.OutPosTagLimit;
			}

			// 标记
			PosTagDTO posTagDTO = new PosTagDTO();
			BeanUtils.copyProperties(posTagTypeDTO, posTagDTO);
			posTagList.add(posTagDTO);

			String newPosTags = JSONArray.toJSONString(posTagList);
			role.setPosTags(newPosTags);

			// 仅更新 posTags 到数据库
			RoleDTO updateRole = new RoleDTO();
			updateRole.setPosTags(newPosTags);
			updateRole.setRid(rid);
			roleDataMgr.updateRoleDataToDB(updateRole);
		} catch (Exception e) {
			return NetResponseCodeConstants.InvalidParam;
		}

		return NetResponseCodeConstants.SUCCESS;
	}
}
