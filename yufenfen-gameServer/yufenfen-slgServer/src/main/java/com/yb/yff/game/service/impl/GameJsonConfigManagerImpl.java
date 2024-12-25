package com.yb.yff.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.GeneralMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.UnionMgrImpl;
import com.yb.yff.game.data.constant.myEnum.BuildType;
import com.yb.yff.game.data.dto.city.NationalMap;
import com.yb.yff.game.data.dto.city.NationalMaps;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.facility.config.*;
import com.yb.yff.game.data.dto.general.GeneralConfigDTO;
import com.yb.yff.game.data.dto.nationMap.ConfigDTO;
import com.yb.yff.game.data.dto.nationMap.config.*;
import com.yb.yff.game.jsondb.data.dto.Basic;
import com.yb.yff.game.jsondb.data.dto.MapRes;
import com.yb.yff.game.jsondb.data.dto.Map_build;
import com.yb.yff.game.jsondb.data.dto.Map_build_custom;
import com.yb.yff.game.jsondb.data.dto.facility.*;
import com.yb.yff.game.jsondb.data.dto.general.*;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_army;
import com.yb.yff.game.jsondb.data.dto.skill.Skills;
import com.yb.yff.game.jsondb.utils.JSonConfigLoader;
import com.yb.yff.game.service.IGameJsonConfigManager;
import com.yb.yff.game.utils.CityPositionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @Class: GameJsonConfigManagerImpl
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: dd
 */
@Service
public class GameJsonConfigManagerImpl implements IGameJsonConfigManager {
	@Value("classpath:data/conf/json/**/*.json")
	private Resource[] resources;

	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	List<IJsonDataHandler> jsonDataHandlers;

	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	GeneralMgrImpl generalMgr;

	@Autowired
	UnionMgrImpl unionMgr;

	private HashMap<String, Object> jSonConfigs = new HashMap();


	@PostConstruct
	public void readAllJsonData() {
		JSonConfigLoader.loadAllJsonConfig(resources, jSonConfigs);

		// 1 加工数据
		processingData();

		// 2 缓存JSON配置数据
		jsonConfigMgr.syncData2Cache(getJsonConfig(Basic.class), getJsonConfig(NationalMaps.class),
				getJsonConfig(NationConfigDTO.class), getJsonConfig(Npc_army.class), getJsonConfig(FacilitiesDTO.class),
				getJsonConfig(GeneralConfigDTO.class), getJsonConfig(Skills.class));


		// 同步数据到DB, role 和 general 数据优先执行初始化
		roleDataMgr.syncData2DB();
		generalMgr.syncData2DB();
		for (IJsonDataHandler dataHandler : jsonDataHandlers) {
			if (roleDataMgr.hashCode() == (dataHandler.hashCode()) ||
					generalMgr.hashCode() == (dataHandler.hashCode()) ||
					unionMgr.hashCode() == (dataHandler.hashCode())) {
				continue;
			}
			dataHandler.syncData2DB();
		}
		unionMgr.syncData2DB();
	}


	/**
	 * 读取JSON配置
	 *
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> T getJsonConfig(Class<T> clazz) {
		String key = clazz.getSimpleName();
		return (T) jSonConfigs.get(key);
	}


	private void processingData() {
		// 加工地图配置数据
		processingMapConfigData();

		// 加工设施数据
		processingFacilityData();

		// 加工地图数据
		processingMapData();

		// 加工军队数据
		processingArmyData();
	}

	/**
	 * 处理军队、将领数据
	 */
	private void processingArmyData() {
		// TODO 军队数据

		GeneralConfigDTO generalConfigDTO = new GeneralConfigDTO();

		List<Integer> generalList = new ArrayList();
		Map<Integer, GeneralList> generalMap = new HashMap();
		getJsonConfig(General.class).getList().forEach(item -> {
			generalList.add(item.getCfgId());
			generalMap.put(item.getCfgId(), item);
		});
		generalConfigDTO.setGeneralList(generalList);
		generalConfigDTO.setGeneralMap(generalMap);

		Map<Integer, General_armsArms> armMap = new HashMap();
		getJsonConfig(General_arms.class).getArms().forEach(item -> {
			armMap.put(item.getId(), item);
		});
		generalConfigDTO.setArmMap(armMap);

		Map<Integer, General_basicLevels> levelMap = new HashMap<>();
		getJsonConfig(General_basic.class).getLevels().forEach(item -> {
			levelMap.put(item.getLevel(), item);
		});
		generalConfigDTO.setLevelMap(levelMap);

		addJsonConfig(generalConfigDTO);

		rmJsonConfig(General.class);
		rmJsonConfig(General_arms.class);
		rmJsonConfig(General_basic.class);
	}

	/**
	 * 处理地图数据
	 */
	private void processingMapData() {
		// 处理地图资源数据
		MapRes mapRes = getJsonConfig(MapRes.class);
		CityPositionUtils.MapWidth = mapRes.getW();
		CityPositionUtils.MapHeight = mapRes.getH();
		CityPositionUtils.SafeDistance = mapRes.getSafeDistance();
		AtomicInteger index = new AtomicInteger();

		Map<Integer, NationalMap> nMapResMap = new HashMap();
		List<NationalMap> sysBuildList = new ArrayList<>();

		mapRes.getResList().forEach(item -> {
			NationalMap map = new NationalMap();
			PositionDTO pos = new PositionDTO();
			pos.setX(index.get() % CityPositionUtils.MapWidth);
			pos.setY(index.get() / CityPositionUtils.MapHeight);
			map.setPosId(index.get());
			map.setPos(pos);
			map.setType(item.get(0));
			map.setLevel(item.get(1));
			nMapResMap.put(map.getPosId(), map);

			// 系统建筑
			if (map.getType() == BuildType.MapBuildSysFortress.getValue() || map.getType() == BuildType.MapBuildSysCity.getValue()) {
				sysBuildList.add(map);
			}

			index.getAndIncrement();
		});

		// 缓存加工后的数据
		NationalMaps nationalMaps = new NationalMaps();
		nationalMaps.setNationalMapMap(nMapResMap);
		nationalMaps.setSysBuildList(sysBuildList);

		addJsonConfig(nationalMaps);

		// 移除未加工的数据
		rmJsonConfig(MapRes.class);
	}

	/**
	 * 处理地图配置数据
	 */
	private void processingMapConfigData() {
		Map_build mapBuild = getJsonConfig(Map_build.class);
		Map_build_custom mapBuildCustom = getJsonConfig(Map_build_custom.class);

		NationConfigDTO nationConfigDTO = new NationConfigDTO();
		BeanUtils.copyProperties(mapBuild, nationConfigDTO);
		nationConfigDTO.setNmcMap(new HashMap());
		nationConfigDTO.setCfg(new ArrayList<>());
		mapBuild.getCfg().forEach(item -> {
			if (!nationConfigDTO.getNmcMap().containsKey(item.getType())) {
				Map<Integer, ConfigDTO> configDTOMap = new HashMap<>();
				nationConfigDTO.getNmcMap().put(item.getType(), configDTOMap);
			}
			ConfigDTO configDTO = new ConfigDTO();
			BeanUtils.copyProperties(item, configDTO);
			configDTO.setLevel(item.getLevel());
			nationConfigDTO.getNmcMap().get(item.getType()).put(item.getLevel(), configDTO);
			nationConfigDTO.getCfg().add(configDTO);
		});

		Map<Integer, MBCustomConfigDTO> mbCustomConfigMap = new HashMap<>();
		nationConfigDTO.setNmccMap(mbCustomConfigMap);

		mapBuildCustom.getCfgc().forEach(item -> {
			MBCustomConfigDTO mbCustomConfig = new MBCustomConfigDTO();
			BeanUtils.copyProperties(item, mbCustomConfig);
			mbCustomConfig.setLevelMap(new HashMap<>());
			item.getLevels().forEach(levelItem -> {
				MBCustomConfigLevelDTO mbCustomConfigLevel = new MBCustomConfigLevelDTO();
				BeanUtils.copyProperties(levelItem, mbCustomConfigLevel);
				MBCustomConfigLevelResultDTO result = new MBCustomConfigLevelResultDTO();
				result.setArmyCnt(levelItem.getResult().getArmy_cnt());
				MBCustomConfigLevelNeedDTO need = new MBCustomConfigLevelNeedDTO();
				BeanUtils.copyProperties(levelItem.getNeed(), need);
				mbCustomConfigLevel.setNeed(need);
				mbCustomConfigLevel.setResult(result);
				mbCustomConfig.getLevelMap().put(levelItem.getLevel(), mbCustomConfigLevel);
			});
			mbCustomConfigMap.put(item.getType(), mbCustomConfig);
		});


		addJsonConfig(nationConfigDTO);
		rmJsonConfig(Map_build.class);
	}

	/**
	 * 处理设施数据
	 */
	private void processingFacilityData() {

		/*******************************************/
		/******** 初始化 设施对游戏的加成数据  *********/
		/*******************************************/
		Map<Integer, FacilityPropertyDTO> facilityPropertyMaps = new HashMap<>();
		List<Facility_configConfigs> facilityConfigs = getJsonConfig(Facility_config.class).getConfigs();

		List<Facility_additionList> additionList = getJsonConfig(Facility_addition.class).getList();

		facilityConfigs.forEach(item -> {
			String itemJSStr = JSONObject.toJSONString(item);
			FacilityPropertyDTO facilityPropertyDTO = JSON.parseObject(itemJSStr, FacilityPropertyDTO.class);

			List<FacilityPropertyAdditionDTO> additionDetails = new ArrayList<>();
			facilityPropertyDTO.getAdditions().forEach(additionItem -> {
				FacilityPropertyAdditionDTO addition = new FacilityPropertyAdditionDTO();
				BeanUtils.copyProperties(additionList.get(additionItem - 1), addition);
				additionDetails.add(addition);
			});
			facilityPropertyDTO.setAdditionDetails(additionDetails);

			facilityPropertyMaps.put(facilityPropertyDTO.getType(), facilityPropertyDTO);
		});


		/*******************************************/
		/************** 初始化 设施列表 **************/
		/*******************************************/
		List<FacilityFacilityList> facilityList = getJsonConfig(Facility.class).getFacilityList();

		List<FacilityDTO> facilitieList = new ArrayList<>();
		Map<Integer, FacilityDTO> facilityMap = new HashMap<>();

		facilityList.forEach(item -> {
			FacilityDTO facility = new FacilityDTO();
			facility.setType(item.getType());
			facility.setName(item.getName());
			facility.setLevel(0);
			facility.setUpTime(0);
			facility.setProperty(facilityPropertyMaps.get(item.getType()));
			facilitieList.add(facility);

			Map<Integer, FacilityPropertyLevel> propertyPLMap = new HashMap<>();

			facility.getProperty().getLevels().forEach(levelItem -> {
				propertyPLMap.put(levelItem.getLevel(), levelItem);
			});

			facility.getProperty().setLeveMap(propertyPLMap);
			facilityMap.put(item.getType(), facility);
		});

		FacilitiesDTO cityFacilitiesDTO = new FacilitiesDTO();
		cityFacilitiesDTO.setFacilities(facilitieList);
		cityFacilitiesDTO.setFacilityMap(facilityMap);

		addJsonConfig(cityFacilitiesDTO);

		// 移除未加工的数据
		rmJsonConfig(Facility.class);
	}

	private <T> void rmJsonConfig(Class<T> clazz) {
		String key = clazz.getSimpleName();
		jSonConfigs.remove(key);
	}

	private <T> void addJsonConfig(T data) {
		String key = data.getClass().getSimpleName();
		jSonConfigs.put(key, data);
	}
}
