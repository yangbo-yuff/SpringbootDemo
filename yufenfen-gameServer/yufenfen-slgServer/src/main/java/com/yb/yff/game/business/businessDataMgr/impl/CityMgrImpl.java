package com.yb.yff.game.business.businessDataMgr.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.constant.myEnum.FacilityType;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.FacilityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.union.UnionDTO;
import com.yb.yff.game.data.entity.MapRoleCityEntity;
import com.yb.yff.game.service.IMapRoleCityService;
import com.yb.yff.game.utils.CityPositionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 地图建筑物管理
 */
@Component
@Slf4j
public class CityMgrImpl implements IJsonDataHandler {
	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	CityFacilityMgrImpl cityFacilityMgr;

	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	IMapRoleCityService mapRoleCityService;

	@Autowired
	UnionMgrImpl unionMgr;

	// DB数据  key db  id
	ConcurrentHashMap<Integer, CityDTO> dbRoleCityMap = new ConcurrentHashMap<>();

	// key : pos Id    CityPositionUtils.position2Number
	ConcurrentHashMap<Integer, CityDTO> posRoleCityMap = new ConcurrentHashMap<>();

	//  key : role Id
	ConcurrentHashMap<Integer, List<CityDTO>> roleRoleCityMap = new ConcurrentHashMap<>();

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {
		if (dbRoleCityMap.size() > 0) {
			return;
		}

		QueryWrapper<MapRoleCityEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("is_main", 1);
		List<MapRoleCityEntity> citys = mapRoleCityService.getBaseMapper().selectList(queryWrapper);
		citys.forEach(city -> {
			CityDTO cityDTO = cityEntity2DTO(city);
			addCityToCache(cityDTO, false);
		});
	}

	private CityDTO cityEntity2DTO(MapRoleCityEntity city) {
		RoleDTO role = roleDataMgr.getRoleDTO(city.getRid());
		FacilityDTO facility = cityFacilityMgr.getFacility(city.getRid(), city.getId(), FacilityType.Main.getValue());

		CityDTO cityDTO = new CityDTO();
		BeanUtils.copyProperties(city, cityDTO);
		cityDTO.setCityId(city.getId());
		cityDTO.setIsMain(city.getIsMain() == 1);

		cityDTO.setMaxDurable(jsonConfigMgr.getBasicConfig().getCity().getDurable());

		UnionDTO union = unionMgr.getUnion(role.getUnionId());
		if(union != null){
			cityDTO.setUnionId(union.getId());
			cityDTO.setUnionName(union.getName());
		}

		cityDTO.setLevel(facility.getLevel());

		return cityDTO;
	}


	private MapRoleCityEntity cityDTO2Entity(CityDTO cityDTO) {
		MapRoleCityEntity cityEntity = new MapRoleCityEntity();

		BeanUtils.copyProperties(cityDTO, cityEntity);
		cityEntity.setId(cityDTO.getCityId());
		cityEntity.setIsMain(cityDTO.getIsMain() ? 1 : 0);

		return cityEntity;
	}


	/**
	 * 创建城市
	 *
	 * @param rid
	 */
	public CityDTO createRoleCityData(Integer rid, PositionDTO cityPosition) {
		RoleDTO role = roleDataMgr.getRoleDTO(rid);

		// TODO 生成主城
		CityDTO cityDTO = new CityDTO();
		cityDTO.setRid(rid);
		cityDTO.setX(cityPosition.getX());
		cityDTO.setY(cityPosition.getY());
		cityDTO.setIsMain(true);
		cityDTO.setCurDurable(jsonConfigMgr.getBasicConfig().getCity().getDurable());
		cityDTO.setMaxDurable(jsonConfigMgr.getBasicConfig().getCity().getDurable());
		cityDTO.setName(role.getNickName());
		cityDTO.setParentId(0);

		UnionDTO union = unionMgr.getUnion(role.getUnionId());
		if(union == null){
			cityDTO.setUnionId(0);
			cityDTO.setUnionName("");
		} else {
			cityDTO.setUnionId(union.getId());
			cityDTO.setUnionName(union.getName());
		}

		cityDTO.setLevel(1);

		if (!addCityToCache(cityDTO, true)) {
			return null;
		}

		return cityDTO;
	}


	/**
	 * 获取城市列表
	 *
	 * @param rid
	 * @return
	 */
	public List<CityDTO> getCitys(Integer rid) {
		return roleRoleCityMap.computeIfAbsent(rid, k -> new ArrayList<>());
	}

	/**
	 * 获取城市列表
	 *
	 * @param cid
	 * @return
	 */
	public CityDTO getCity(Integer cid) {
		return dbRoleCityMap.get(cid);
	}

	/**
	 * 添加一个城市到缓存
	 *
	 * @param cityDTO
	 */
	public boolean addCityToCache(CityDTO cityDTO, boolean isToDB) {
		// DB
		if (isToDB) {
			if (!addCityToDB(cityDTO)) {
				return false;
			}
		}

		// cache
		dbRoleCityMap.put(cityDTO.getCityId(), cityDTO);

		posRoleCityMap.put(CityPositionUtils.position2Number(cityDTO.getX(), cityDTO.getY()), cityDTO);

		roleRoleCityMap.computeIfAbsent(cityDTO.getRid(), k -> new ArrayList<>()).add(cityDTO);

		return true;
	}

	/**
	 * 获取全部主城
	 *
	 * @return
	 */
	public List<CityDTO> getAllCitys() {
		return roleRoleCityMap.values().stream()
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	public CityDTO getPositionCity(PositionDTO pos) {
		int posId = CityPositionUtils.position2Number(pos);
		return posRoleCityMap.get(posId);
	}

	public CityDTO getPositionCity(int x, int y) {
		int posId = CityPositionUtils.position2Number(x, y);
		return posRoleCityMap.get(posId);
	}

	/**
	 * 是否是免战状态
	 *
	 * @param city
	 * @return
	 */
	public boolean isWarFree(CityDTO city) {
		Long curTime = System.currentTimeMillis();
		if (curTime - city.getOccupyTime().getTime() < jsonConfigMgr.getBasicConfig().getBuild().getWar_free() * 1000) {
			return true;
		} else {
			return false;
		}
	}

	public void durableChange(CityDTO city, Integer change) {

		Integer current = city.getCurDurable() + change;
		if (current < 0) {
			city.setCurDurable(0);
		} else {
			city.setCurDurable(Math.min(city.getMaxDurable(), current));
		}
	}

	/**
	 * 添加一个城市到数据库
	 *
	 * @param cityDTO
	 * @return
	 */
	private boolean addCityToDB(CityDTO cityDTO) {
		MapRoleCityEntity mapRoleCityEntity = cityDTO2Entity(cityDTO);

		//上传数据
		if (mapRoleCityService.getBaseMapper().insert(mapRoleCityEntity) == 0) {
			log.error("主城： " + cityDTO.getName() + " 插入数据库失败");
			return false;
		}

		cityDTO.setCityId(mapRoleCityEntity.getId());
		return true;
	}
}
