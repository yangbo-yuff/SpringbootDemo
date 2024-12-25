package com.yb.yff.game.business.businessDataMgr.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.data.entity.CityFacilityEntity;
import com.yb.yff.game.data.entity.MapRoleCityEntity;
import com.yb.yff.game.data.dto.facility.config.FacilityDTO;
import com.yb.yff.game.data.dto.facility.config.FacilityPropertyDTO;
import com.yb.yff.game.service.ICityFacilityService;
import com.yb.yff.game.data.dto.city.CityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
 * @Class: CityFacilityMgrImpl
 * @CreatedOn 2024/11/6.
 * @Email: yangboyff@gmail.com
 * @Description: 主城数据管理
 */
@Component
@Slf4j
public class CityFacilityMgrImpl implements IJsonDataHandler {
	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	IRoleLogic roleLogic;

	@Autowired
	ICityFacilityService cityFacilityService;

	@Autowired
	BuildMgrImpl buildMgrImpl;

	@Autowired
	CityMgrImpl cityMgrImpl;

	/**
	 * 主城设施
	 * <roleId, <cityId, <fType, FacilitiesDTO>>>
	 */
	private Map<Integer, Map<Integer, Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO>>> roleCityFacilityMap = new ConcurrentHashMap<>();

	/**
	 *
	 */
	private List<com.yb.yff.game.data.dto.city.FacilityDTO> newFacilityConfigList;

	/**
	 * key: FacilityDTO.type
	 */
	private Map<Integer, FacilityDTO> facilityConfigMap;

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {

		if (roleCityFacilityMap.size() > 0) {
			return;
		}

		facilityConfigMap = jsonConfigMgr.getFacilitiesConfig().getFacilityMap();
		newFacilityConfigList = new ArrayList<>();
		jsonConfigMgr.getFacilitiesConfig().getFacilities().forEach(facility -> {
			com.yb.yff.game.data.dto.city.FacilityDTO facilityDTO = new com.yb.yff.game.data.dto.city.FacilityDTO();
			BeanUtils.copyProperties(facility, facilityDTO);
			newFacilityConfigList.add(facilityDTO);
		});

		List<CityFacilityEntity> dbDatas = cityFacilityService.list();
		if (dbDatas.size() == 0) {
			return;
		}

		initData(dbDatas);
	}

	private void initData(List<CityFacilityEntity> cityFacilityEntities) {
		cityFacilityEntities.forEach(cityFac -> {
			Integer rid = cityFac.getRid();
			Integer cid = cityFac.getCityId();

			List<com.yb.yff.game.data.dto.city.FacilityDTO> facilityDTOs = JSON.parseObject(cityFac.getFacilities(), new TypeReference<List<com.yb.yff.game.data.dto.city.FacilityDTO>>() {
			});

			putFacilities(rid, cid, facilityDTOs);
		});
	}

	/**
	 * 创建新设施
	 *
	 * @return
	 */
	public Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> newCityFacilityMap() {

		Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> facilityMap = new HashMap();

		newFacilityConfigList.forEach(item -> {
			com.yb.yff.game.data.dto.city.FacilityDTO facilityDTO = new com.yb.yff.game.data.dto.city.FacilityDTO();
			BeanUtils.copyProperties(item, facilityDTO);
			facilityMap.put(item.getType(), facilityDTO);
		});

		return facilityMap;
	}

	/**
	 * 创建新设施
	 *
	 * @return
	 */
	public List<com.yb.yff.game.data.dto.city.FacilityDTO> newCityFacilityList() {

		List<com.yb.yff.game.data.dto.city.FacilityDTO> facilityList = new ArrayList<>();

		newFacilityConfigList.forEach(item -> {
			com.yb.yff.game.data.dto.city.FacilityDTO facilityDTO = new com.yb.yff.game.data.dto.city.FacilityDTO();
			BeanUtils.copyProperties(item, facilityDTO);
			facilityList.add(facilityDTO);
		});

		return facilityList;
	}

	/**
	 * 获取设施 配置
	 *
	 * @param fType
	 * @return
	 */
	public FacilityPropertyDTO getFacilityConfig(Integer fType) {
		return jsonConfigMgr.getFacilitiesConfig().getFacilityMap().get(fType).getProperty();
	}

	/**
	 * 获取设施
	 *
	 * @param rid
	 * @param cityId
	 * @return
	 */
	public Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> getFacilityMap(Integer rid, Integer cityId) {
		return roleCityFacilityMap.computeIfAbsent(rid, k -> new ConcurrentHashMap<>()).computeIfAbsent(cityId, k -> new ConcurrentHashMap<>());
	}

	/**
	 * 获取设施
	 *
	 * @param rid
	 * @param cityId
	 * @return
	 */
	public List<com.yb.yff.game.data.dto.city.FacilityDTO> getFacilityList(Integer rid, Integer cityId) {
		return Optional.ofNullable(roleCityFacilityMap)
				.map(map -> map.getOrDefault(rid, Map.of()))
				.map(map -> map.getOrDefault(cityId, Map.of()))
				.orElse(null)
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	/**
	 * 添加主城设施
	 *
	 * @param rid
	 * @param cityId
	 * @return
	 */
	public synchronized void putFacilities(Integer rid, Integer cityId, Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> cityFacilityMap) {
		roleCityFacilityMap.computeIfAbsent(rid, k -> new ConcurrentHashMap<>()).put(cityId, cityFacilityMap);
	}

	/**
	 * 添加主城设施
	 *
	 * @param rid
	 * @param cityId
	 * @return
	 */
	public synchronized void putFacilities(Integer rid, Integer cityId, List<com.yb.yff.game.data.dto.city.FacilityDTO> facilityList) {
		Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> facilityMap = new HashMap<>();
		facilityList.forEach(facilityDTO -> {
			facilityMap.put(facilityDTO.getType(), facilityDTO);
		});

		putFacilities(rid, cityId, facilityMap);
	}

	/**
	 * 获取设施
	 *
	 * @param rid
	 * @param cityId
	 * @param fType
	 * @return
	 */
	public com.yb.yff.game.data.dto.city.FacilityDTO getFacility(Integer rid, Integer cityId, Integer fType) {
		return Optional.ofNullable(roleCityFacilityMap)
				.map(map -> map.getOrDefault(rid, Map.of()))
				.map(map -> map.getOrDefault(cityId, Map.of()))
				.map(map -> map.get(fType))
				.orElse(null);
	}

	/**
	 * 创建角色主城
	 *
	 * @param cityDTO
	 * @return
	 */
	public boolean createRoleCityFacilityData(CityDTO cityDTO) {
		MapRoleCityEntity mapRoleCityEntity = new MapRoleCityEntity();
		BeanUtils.copyProperties(cityDTO, mapRoleCityEntity);

		if (cityDTO.getCityId() == null && cityDTO.getCityId() == 0) {
			return false;
		}

		// 创建主城设施
		Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> facilityMap = newCityFacilityMap();

		return addCityFacilityDataToCache(cityDTO.getRid(), cityDTO.getCityId(), facilityMap, true);
	}

	/**
	 * 更新数据到数据库
	 *
	 * @param rid
	 * @param cityId
	 * @return
	 */
	public boolean addCityFacilityDataToCache(Integer rid, Integer cityId, Map<Integer, com.yb.yff.game.data.dto.city.FacilityDTO> facilityMap, boolean toDB) {
		if (toDB) {
			List<com.yb.yff.game.data.dto.city.FacilityDTO> facilityDTOList = facilityMap.entrySet()
					.stream()
					.map(Map.Entry::getValue)
					.collect(Collectors.toList());

			CityFacilityEntity cityFacilityEntity = new CityFacilityEntity();
			cityFacilityEntity.setRid(rid);
			cityFacilityEntity.setCityId(cityId);
			cityFacilityEntity.setFacilities(JSON.toJSONString(facilityDTOList));

			if (!cityFacilityService.saveOrUpdate(cityFacilityEntity)) {
				log.error("主城ID： " + cityId + " 的设备数据 插入数据库失败");
				return false;
			}
		}

		putFacilities(rid, cityId, facilityMap);

		return true;
	}

	/**
	 * 更新设备数据到数据库
	 *
	 * @param rid
	 * @param cid
	 * @return
	 */
	public boolean updateFacilities2DB(Integer rid, Integer cid) {
		List<com.yb.yff.game.data.dto.city.FacilityDTO> facilities = getFacilityList(rid, cid);
		String facilitiesStr = JSONArray.toJSONString(facilities);

		CityFacilityEntity cityFacilityEntity = new CityFacilityEntity();
		cityFacilityEntity.setFacilities(facilitiesStr);

		UpdateWrapper<CityFacilityEntity> updateWrapper = new UpdateWrapper();
		updateWrapper.eq("rid", rid);
		updateWrapper.eq("city_id", cid);

		return cityFacilityService.update(cityFacilityEntity, updateWrapper);
	}

	/**
	 * 获取城内设施加成
	 * @param rid
	 * @param cid
	 * @param additionTypes
	 * @return
	 */
	public List<Integer> getAdditions(Integer rid, Integer cid, Integer... additionTypes) {
		List<Integer> additionList = new ArrayList<>();
		List<com.yb.yff.game.data.dto.city.FacilityDTO> cityFacilities = getFacilityList(rid, cid);
		if(cityFacilities == null || cityFacilities.size() == 0 ){
			return additionList;
		}

		for (Integer additionType : additionTypes) {
            int total = 0;

            for (com.yb.yff.game.data.dto.city.FacilityDTO f : cityFacilities) {
                if (f.getLevel() > 0) {
                    List<Integer> additions = jsonConfigMgr.getFacilitiesConfigAdditions(f.getType());
                    List<Integer> values = jsonConfigMgr.getFacilitiesConfigValues(f.getType(), f.getLevel());

                    for (int i = 0; i < additions.size(); i++) {
                        if (additions.get(i) == additionType) {
                            total += values.get(i);
                        }
                    }
                }
            }
            additionList.add(total);
        }

        return additionList;
	}
}
