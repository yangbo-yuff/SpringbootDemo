package com.yb.yff.game.business.businessLogic.impl;

import com.yb.yff.game.business.businessDataMgr.impl.BuildMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityFacilityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessLogic.ICityFacilityLogic;
import com.yb.yff.game.business.businessLogic.ICityLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.dto.facility.config.FacilityPropertyDTO;
import com.yb.yff.game.data.dto.facility.config.FacilityPropertyLevel;
import com.yb.yff.game.data.dto.facility.config.FacilityPropertyLevelNeedDTO;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.facility.UpFacilityTaskTCTP;
import com.yb.yff.game.data.dto.city.*;
import com.yb.yff.game.data.dto.role.RoleResourceDTO;
import com.yb.yff.game.data.dto.role.RoleResourceData;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.utils.TaskUitls;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
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
 * @Class: CityLogicImpl
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 主城业务逻辑
 */
@Service
@Slf4j
public class CityLogicImpl extends BusinessDataSyncImpl<CityDTO> implements ICityLogic {

	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	IRoleLogic roleLogic;

	@Autowired
	CityFacilityMgrImpl cityFacilityMgrImpl;

	@Autowired
	ICityFacilityLogic cityFacilityLogic;

	@Autowired
	BuildMgrImpl buildMgrImpl;

	@Autowired
	CityMgrImpl cityMgrImpl;

	@Autowired
	IPushService pushService;

	@PostConstruct
	public void init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_roleCity, pushService);
	}

	/**
	 * 获取角色-主城-设施
	 *
	 * @param facilityResDTO
	 * @param rid
	 * @param cityID
	 * @return
	 */
	@Override
	public ResponseCode getRoleCityFacilities(FacilityResDTO facilityResDTO, Integer rid, Integer cityID) {

		List<FacilityDTO> facilities = cityFacilityMgrImpl.getFacilityList(rid, cityID);

		if (facilities == null || facilities.isEmpty()) {
			facilities = cityFacilityMgrImpl.newCityFacilityList();
		}

		if (facilities == null) {
			return NetResponseCodeConstants.DBError;
		}

		facilityResDTO.setCityId(cityID);
		facilityResDTO.setFacilities(facilities);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 创建设备升级任务
	 *
	 * @param reqDTO
	 * @param roleId
	 * @param upFacilityDTO
	 * @return
	 */
	public LogicTaskResultDTO<TimeConsumingTask> createUpFacilityTCT(GameMessageEnhancedReqDTO reqDTO, Integer roleId, UpFacilityDTO upFacilityDTO) {
		Integer cityId = upFacilityDTO.getCityId();
		Integer fType = upFacilityDTO.getFType();

		LogicTaskResultDTO<TimeConsumingTask> createTCTResultDTO = new LogicTaskResultDTO<>();
		LogicTaskResultDTO<FacilityPropertyLevel> result = checkConditionAndNeed(roleId, cityId, fType);

		if (result.getCode() != NetResponseCodeConstants.SUCCESS) {
			createTCTResultDTO.setCode(result.getCode());
			return createTCTResultDTO;
		}

		FacilityPropertyLevel newPropertyLevel = result.getResult();
		FacilityPropertyLevelNeedDTO need = newPropertyLevel.getNeed();

		Duration delay = Duration.ofSeconds(newPropertyLevel.getTime() - 1); // 服务端提前一秒完成
		String taskKey = TaskUitls.createTaskKey(reqDTO);

		// 花费 资源
		RoleResourceDTO roleResource = new RoleResourceDTO();
		BeanUtils.copyProperties(need, roleResource);
		roleLogic.updateRoleResource(roleId, roleResource, false);

		// 封装返回数据
		FacilityDTO facilityData = cityFacilityMgrImpl.getFacility(roleId, cityId, fType);
		Long upTime = System.currentTimeMillis() / 1000;// 开始时间，不是结束时间 + newPropertyLevel.getTime();
		facilityData.setUp_time(upTime.intValue());

		// 封装延时任务完成时，返回数据
		UpFacilitieResDTO upFacilitieResDTO = new UpFacilitieResDTO();
		upFacilitieResDTO.setFacility(facilityData);
		upFacilitieResDTO.setCityId(cityId);
		upFacilitieResDTO.setRole_res(roleLogic.getRoleResource(roleId));
		upFacilitieResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		GameMessageEnhancedResDTO gameMessageEnhancedResDTO = new GameMessageEnhancedResDTO();
		BeanUtils.copyProperties(reqDTO, gameMessageEnhancedResDTO);

		gameMessageEnhancedResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		gameMessageEnhancedResDTO.setMsg(upFacilitieResDTO);

		UpFacilityTaskTCTP upFacilityTaskTCTP = new UpFacilityTaskTCTP();
		upFacilityTaskTCTP.setRid(roleId);
		upFacilityTaskTCTP.setCityId(cityId);
		upFacilityTaskTCTP.setFType(fType);
		upFacilityTaskTCTP.setFacilityPropertyLevel(newPropertyLevel);

		TimeConsumingTask timeConsumingTask = new TimeConsumingTask<UpFacilityTaskTCTP>(taskKey, delay, "",
				gameMessageEnhancedResDTO, null, upFacilityTaskTCTP, (param) -> {
			UpFacilityTaskTCTP upFParam = (UpFacilityTaskTCTP) param;
			// 更新缓存
			cityFacilityLogic.updataFacility(upFParam);
			log.info("================== 耗时任务 【{}秒 将 城市{} 的 设施{} 升级到 {}级】 执行完成！",
					upFParam.getFacilityPropertyLevel().getTime(),
					upFParam.getCityId(),
					upFParam.getFType(),
					upFParam.getFacilityPropertyLevel().getLevel());
		});

		createTCTResultDTO.setCode(NetResponseCodeConstants.SUCCESS);
		createTCTResultDTO.setResult(timeConsumingTask);
		return createTCTResultDTO;
	}

	/**
	 * 安全创建城市
	 *
	 * @param rid
	 */
	@Override
	public synchronized boolean createCitySafely(Integer rid) {
		List<CityDTO> cityList = cityMgrImpl.getCitys(rid);

		if (cityList != null && cityList.size() > 0) {
			// 主城已经存在
			return false;
		}

		return createCity(rid);
	}


	/**
	 * 获取角色主城
	 */
	@Override
	public CityDTO getMainCitys(Integer rid) {
		List<CityDTO> cityList = cityMgrImpl.getCitys(rid);

		for (CityDTO city : cityList) {
			if (city.getIs_main()) {
				return city;
			}
		}

		return null;
	}

	/**
	 * 检查设备升级条件
	 *
	 * @param rid
	 * @param cityId
	 * @param fType
	 * @return
	 */
	private LogicTaskResultDTO<FacilityPropertyLevel> checkConditionAndNeed(Integer rid, Integer cityId, Integer fType) {
		LogicTaskResultDTO<FacilityPropertyLevel> resultDTO = new LogicTaskResultDTO<FacilityPropertyLevel>();

		List<CityDTO> roleCityList = cityMgrImpl.getCitys(rid);
		Map<Integer, FacilityDTO> roleCtiyFacilityMap = cityFacilityMgrImpl.getFacilityMap(rid, cityId);

		if (roleCtiyFacilityMap == null || roleCtiyFacilityMap.size() == 0 || roleCityList == null || roleCityList.size() == 0) {
			resultDTO.setCode(NetResponseCodeConstants.CityNotExist);
			return resultDTO;
		}

		if (roleCtiyFacilityMap.size() == 0) {
			resultDTO.setCode(NetResponseCodeConstants.CityNotExist);
			return resultDTO;
		}

		// 获取目标设施数据
		FacilityDTO facilityDTO = roleCtiyFacilityMap.get(fType);
		if (facilityDTO == null) {
			resultDTO.setCode(NetResponseCodeConstants.CityNotExist);
			return resultDTO;
		}

		FacilityPropertyDTO facilitiesBO = cityFacilityMgrImpl.getFacilityConfig(fType);

		// 级别检查
		Map<Integer, FacilityPropertyLevel> levels = facilitiesBO.getLeveMap();
		Integer maxLevel = levels.size();
		if (facilityDTO.getLevel() >= maxLevel) {
			resultDTO.setCode(NetResponseCodeConstants.UpError);
			return resultDTO;
		}

		// 检测其它设施的等级条件
		AtomicInteger checkOhterFacitlityFalse = new AtomicInteger();
		facilitiesBO.getConditions().stream().forEach(condition -> {
			FacilityDTO conditionFDTO = roleCtiyFacilityMap.get(condition.getType());
			if (conditionFDTO.getLevel() < condition.getLevel()) {
				checkOhterFacitlityFalse.getAndIncrement();
			}
		});
		if (checkOhterFacitlityFalse.get() > 0) {
			resultDTO.setCode(NetResponseCodeConstants.UpError);
			return resultDTO;
		}

		// 检查资源是否足够
		RoleResourceData roleResourceData = roleLogic.getRoleResource(rid);
		int newLevel = facilityDTO.getLevel() + 1;
		FacilityPropertyLevel newPropertyLevel = levels.get(newLevel);
		FacilityPropertyLevelNeedDTO need = newPropertyLevel.getNeed();
		if (need.getDecree() > roleResourceData.getDecree() || need.getIron() > roleResourceData.getIron() ||
				need.getWood() > roleResourceData.getWood() || need.getGrain() > roleResourceData.getGrain() ||
				need.getStone() > roleResourceData.getStone()) {
			resultDTO.setCode(NetResponseCodeConstants.UpError);
			return resultDTO;
		}

		resultDTO.setCode(NetResponseCodeConstants.SUCCESS);
		resultDTO.setResult(newPropertyLevel);
		return resultDTO;
	}


	/**
	 * 创建城市
	 *
	 * @param rid
	 */
	private synchronized boolean createCity(Integer rid) {
		List<CityDTO> cityList = cityMgrImpl.getAllCitys();

		// 获取城市位置
		PositionDTO cityPosition = CityPositionUtils.getIdealPosition(cityList);

		// 创建角色主城
		CityDTO cityDTO = cityMgrImpl.createRoleCityData(rid, cityPosition);
		if (cityDTO == null) {
			return false;
		}

		// 创建角色主城设施
		cityFacilityMgrImpl.createRoleCityFacilityData(cityDTO);
		return true;
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param city
	 */
	@Override
	public void syncExecute(Integer rid, CityDTO city) {
		// 2 DB

		// 2 client
		pushData(rid, city);
	}
}
