package com.yb.yff.game.business;

import com.yb.yff.game.jsondb.data.dto.Basic;
import com.yb.yff.game.service.IGameRoleDataManager;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.utils.TaskUitls;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.bo.FacilityBO;
import com.yb.yff.sb.data.bo.FacilityPropertyLevel;
import com.yb.yff.sb.data.bo.FacilityPropertyLevelNeed;
import com.yb.yff.sb.data.bo.RoleBO;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.data.dto.city.*;
import com.yb.yff.sb.data.dto.role.RoleResourceData;
import com.yb.yff.sb.taskCallback.DelayedExecuteTask;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
 * @Description: 游戏城市业务处理
 */
@Component
public class CityBusinessHandler {

	@Autowired
	private IGameRoleDataManager gameRoleDataManager;

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

		CityBaseDTO dto = (CityBaseDTO) reqDTO.getMsg();
		Integer cityID = dto.getCityId();

		CityFacilityData cityFacilityData = gameRoleDataManager.getRoleData(rid, CityFacilityData.class);

		List<FacilityBO> facilities = null;

		if (cityFacilityData.getFacilities() == null) {
			facilities = gameRoleDataManager.createCityFacilitiesData(cityID);
		} else {

			facilities = cityFacilityData.getFacilities().get(cityID);
		}

		if (facilities == null) {
			facilityResDTO.setCode(NetResponseCodeConstants.DBError.getCode());
			return facilityResDTO;
		}

		List<FacilityDTO> facilityList = new ArrayList<>();
		facilities.stream().forEach(facility -> {
			FacilityDTO facilityDTO = new FacilityDTO();
			BeanUtils.copyProperties(facility, facilityDTO);
			facilityList.add(facilityDTO);
		});

		facilityResDTO.setCityId(cityID);
		facilityResDTO.setFacilities(facilityList);


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

		UpFacilityDTO upFacilityDTO = (UpFacilityDTO) reqDTO.getMsg();
		Integer cityId = upFacilityDTO.getCityId();
		Integer fType = upFacilityDTO.getFType();

		CityData cityData = gameRoleDataManager.getRoleData(rid, CityData.class);
		CityFacilityData cityFacilityData = gameRoleDataManager.getRoleData(rid, CityFacilityData.class);
		if (cityFacilityData == null || cityData == null) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.CityNotExist.getCode());
			return upFacilitieResDTO;
		}

		if (cityFacilityData.getRid() != rid) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.CityNotMe.getCode());
			return upFacilitieResDTO;
		}

		List<FacilityBO> facilityList = cityFacilityData.getFacilities().get(cityId);

		if (facilityList == null || facilityList.size() == 0) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.CityNotExist.getCode());
			return upFacilitieResDTO;
		}

		// 获取目标设施数据
		FacilityBO facilityBO = facilityList.get(fType);
		if (facilityBO == null) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.CityNotExist.getCode());
			return upFacilitieResDTO;
		}

		/**********************************/
		/***********   前置条件   **********/
		/**********************************/

		// 级别检查
		List<FacilityPropertyLevel> levels = facilityBO.getProperty().getLevels();
		Integer maxLevel = levels.size();
		if (facilityBO.getLevel() >= maxLevel) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.UpError.getCode());
			return upFacilitieResDTO;
		}

		// 检测其它设施条件
		AtomicInteger checkOhterFacitlityFalse = new AtomicInteger();
		facilityBO.getProperty().getConditions().stream().forEach(condition -> {
			FacilityBO conditionFBO = facilityList.get(condition.getType());
			if (conditionFBO.getLevel() < condition.getLevel()) {
				checkOhterFacitlityFalse.getAndIncrement();
			}
		});
		if (checkOhterFacitlityFalse.get() > 0) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.UpError.getCode());
			return upFacilitieResDTO;
		}

		// 检查资源是否足够
		RoleResourceData roleResourceData = gameRoleDataManager.getRoleData(rid, RoleResourceData.class);
		int newLevel = facilityBO.getLevel() + 1;
		FacilityPropertyLevel newPropertyLevel = levels.get(newLevel);
		FacilityPropertyLevelNeed need = newPropertyLevel.getNeed();
		if (need.getDecree() > roleResourceData.getDecree() || need.getIron() > roleResourceData.getIron() ||
				need.getWood() > roleResourceData.getWood() || need.getGrain() > roleResourceData.getGrain() ||
				need.getStone() > roleResourceData.getStone()) {
			upFacilitieResDTO.setCode(NetResponseCodeConstants.UpError.getCode());
			return upFacilitieResDTO;
		}

		// TODO 更新耗时 启动计算器 ？ 开关处发起计时任务
		Duration delay = Duration.ofSeconds(newPropertyLevel.getTime());
		String taskKey = TaskUitls.createTaskKey(reqDTO);
		DelayedExecuteTask delayedTask = new DelayedExecuteTask(taskKey, delay, "", (geResDTO) -> {
			// 更新资源
			roleResourceData.setDecree(roleResourceData.getDecree() - need.getDecree());
			roleResourceData.setIron(roleResourceData.getIron() - need.getIron());
			roleResourceData.setWood(roleResourceData.getWood() - need.getWood());
			roleResourceData.setGrain(roleResourceData.getGrain() - need.getGrain());
			roleResourceData.setStone(roleResourceData.getStone() - need.getStone());
			gameRoleDataManager.updateRoleData(roleResourceData, true);

			// 更新缓存
			FacilityData facilityData = gameRoleDataManager.getRoleData(rid, FacilityData.class);
			facilityData.setLevel(newLevel);
			facilityData.setUp_time(newPropertyLevel.getTime());
			gameRoleDataManager.updateRoleData(facilityData, true);

			// 封装返回数据
			FacilityDTO facilityDTO = new FacilityDTO();
			BeanUtils.copyProperties(facilityData, facilityDTO);
			upFacilitieResDTO.setFacilitie(facilityDTO);
			upFacilitieResDTO.setCityId(cityId);
			upFacilitieResDTO.setRole_res(roleResourceData);

			upFacilitieResDTO.setDelayedTask(null);
			upFacilitieResDTO.setCode(null);

			GameMessageEnhancedResDTO gameMessageEnhancedResDTO = new GameMessageEnhancedResDTO();
			BeanUtils.copyProperties(reqDTO, gameMessageEnhancedResDTO);

			gameMessageEnhancedResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
			gameMessageEnhancedResDTO.setMsg(upFacilitieResDTO);

			return gameMessageEnhancedResDTO;
		});

		upFacilitieResDTO.setDelayedTask(delayedTask);

		return upFacilitieResDTO;
	}


	/**
	 * 安全创建城市
	 *
	 * @param rid
	 */
	public synchronized void createCitySafely(Integer rid) {
		CityData cityData = gameRoleDataManager.getRoleData(rid, CityData.class);
		if (cityData == null) {
			// 角色不存在
			return;
		}

		if (cityData.getCityList() != null && cityData.getCityList().size() > 0) {
			// 主城已经存在
			return;
		}

		createCity(rid);
	}


	/**
	 * 创建城市
	 *
	 * @param rid
	 */
	private synchronized void createCity(Integer rid) {
		NationalMaps nationalMaps = gameRoleDataManager.getJsonConfig(NationalMaps.class);

		List<CityData> citys = gameRoleDataManager.getAllRoleData(rid, CityData.class);

		PositionDTO cityPosition = CityPositionUtils.getIdealPosition(citys, nationalMaps);

		Basic basic = gameRoleDataManager.getJsonConfig(Basic.class);
		RoleBO roleBO = gameRoleDataManager.getRoleVO(rid);

		// TODO 生成主城
		CityDTO cityDTO = new CityDTO();
		cityDTO.setRid(rid);
		cityDTO.setX(cityPosition.getX());
		cityDTO.setY(cityPosition.getY());
		cityDTO.setIs_main(1);
		cityDTO.setCur_durable(basic.getCity().getDurable());
		cityDTO.setName(roleBO.getNickName());

		// 保存数据
		gameRoleDataManager.createRoleCityData(cityDTO);
	}
}
