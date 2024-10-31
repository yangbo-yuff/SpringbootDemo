package com.yb.yff.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.data.entity.CityFacilityEntity;
import com.yb.yff.game.data.entity.MapRoleCityEntity;
import com.yb.yff.game.data.entity.RoleEntity;
import com.yb.yff.game.service.*;
import com.yb.yff.sb.data.bo.FacilitiesBO;
import com.yb.yff.sb.data.bo.FacilityBO;
import com.yb.yff.sb.data.bo.RoleBO;
import com.yb.yff.sb.data.dto.city.CityDTO;
import com.yb.yff.sb.data.dto.city.CityData;
import com.yb.yff.sb.data.dto.city.CityFacilityData;
import com.yb.yff.sb.data.dto.role.CreateDTO;
import com.yb.yff.sb.data.dto.role.RoleData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: service
 * @CreatedOn 2024/10/24.
 * @Email: yangboyff@gmail.com
 * @Description: jsondb 服务，提供从JSon文件读取的数据
 */
@Service
public class GameRoleDataManagerImpl implements IGameRoleDataManager {

	@Autowired
	private IRoleService roleService;

	@Autowired
	private ICityFacilityService cityFacilityService;

	@Autowired
	private IMapRoleCityService mapRoleCityService;

	@Autowired
	IGameJsonConfigManager jsonConfigManager;

	/**
	 * 用户<->角色 缓存
	 * <userId,rid>
	 */
	private ConcurrentHashMap<Integer, Integer> userRoleMap = new ConcurrentHashMap<>();

	/**
	 * 角色数据缓存
	 * <rid, roleVO>
	 */
	private ConcurrentHashMap<Integer, RoleBO> roleDataMap = new ConcurrentHashMap<>();

	/**
	 * 读取JSON配置
	 *
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> T getJsonConfig(Class<T> clazz) {
		return jsonConfigManager.getJsonConfig(clazz);
	}

	/**
	 * 从数据库中读取角色ID
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public Integer getRoleId(Integer userId) {
		return userRoleMap.computeIfAbsent(userId, this::getRoleFromDBByUserId);
	}

	/**
	 * 获取角色数据
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public <T extends RoleData> T getRoleDataByUserId(Integer userId, Class<T> clazz) {
		Integer rid = getRoleId(userId);
		if (rid == null) {
			return null;
		}

		return getRoleData(rid, clazz);
	}

	/**
	 * 获取角色数据
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public synchronized RoleBO getRoleVO(Integer rid) {
		return roleDataMap.computeIfAbsent(rid, this::getRoleFromDB);
	}

	/**
	 * 获取角色的具体 (T) 数据
	 *
	 * @param rid
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@Override
	public synchronized <T extends RoleData> T getRoleData(Integer rid, Class<T> clazz) {
		RoleBO roleBO = getRoleVO(rid);
		if (roleBO == null) {
			// 不存在角色
			return null;
		}

		try {
			T roleData = clazz.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(roleBO, roleData);
			roleData.setRid(roleBO.getId());
			return roleData;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 异常返回
		return null;
	}

	/**
	 * 获取全部角色指定类型的数据
	 *
	 * @param rid
	 * @param clazz
	 * @return
	 */
	@Override
	public <T extends RoleData> List<T> getAllRoleData(Integer rid, Class<T> clazz) {
		List<T> roleDatas = new ArrayList<>();

		roleDataMap.entrySet().stream().forEach(entry -> {
			if (rid == entry.getKey()) {
				return;
			}

			RoleBO roleBO = entry.getValue();
			T roleData = null;
			try {
				roleData = clazz.getDeclaredConstructor().newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
			BeanUtils.copyProperties(roleBO, roleData);
			roleData.setRid(roleBO.getId());

			roleDatas.add(roleData);
		});

		return roleDatas;
	}

	/**
	 * 更新角色数据,
	 * 产量数据没有持久化，故只更新缓存
	 *
	 * @param roleData
	 * @param isUpdateDB
	 * @param <T>
	 */
	@Override
	public synchronized <T extends RoleData> boolean updateRoleData(T roleData, boolean isUpdateDB) {
		if (roleData == null) {
			return false;
		}

		RoleBO roleBO = getRoleVO(roleData.getRid());
		if (roleBO == null) {
			return false;
		}

		// 更新缓存
		BeanUtils.copyProperties(roleData, roleBO);

		if (isUpdateDB) {
			if(roleData instanceof CityFacilityData){
				return updateCityFacilityDataToDB(roleBO);
			}

			return updateRoleDataToDB(roleData.getRid(), roleData);
		}

		return false;
	}

	/**
	 * 创建角色
	 *
	 * @param createDTO
	 * @return
	 */
	@Override
	public RoleEntity createRoleData(CreateDTO createDTO) {
		RoleEntity roleEntity = createRoleToDB(createDTO);
		if (roleEntity == null) {
			return null;
		}

		// 插入缓存
		RoleBO roleBO = new RoleBO();
		BeanUtils.copyProperties(roleEntity, roleBO);
		roleDataMap.put(createDTO.getUid(), roleBO);

		return roleEntity;
	}

	/**
	 * 创建角色主城
	 *
	 * @param cityDTO
	 * @return
	 */
	@Override
	public boolean createRoleCityData(CityDTO cityDTO) {
		MapRoleCityEntity mapRoleCityEntity = new MapRoleCityEntity();
		BeanUtils.copyProperties(cityDTO, mapRoleCityEntity);

		if (mapRoleCityService.getBaseMapper().insert(mapRoleCityEntity) == 0) {
			return false;
		}
		cityDTO.setCityId(mapRoleCityEntity.getId());

		// 创建主城设施
		createCityFacilitiesData(cityDTO.getCityId());

		// 更新缓存
		CityData cityData = new CityData();
		cityData.setRid(cityDTO.getRid());
		ArrayList<CityDTO> cityList = new ArrayList<CityDTO>();
		cityList.add(cityDTO);
		cityData.setCityList(cityList);

		// 更新缓存数据，由于新建记录，故无需持久化
		updateRoleData(cityData, false);

		return true;
	}

	/**
	 * 创建主城设施数据, 创建主城时创建
	 *
	 * @param cityId
	 * @return
	 */
	@Override
	public List<FacilityBO> createCityFacilitiesData(Integer cityId) {
		// 主城 设施 数据
		FacilitiesBO facilitiesBO = this.getJsonConfig(FacilitiesBO.class);
		if(facilitiesBO == null){
			return null;
		}

		// 更新缓存
		CityFacilityData cityFacilityData = new CityFacilityData();
		cityFacilityData.getFacilities().put(cityId, facilitiesBO.getFacilities());
		this.updateRoleData(cityFacilityData, true);

		return facilitiesBO.getFacilities();
	}

	/**
	 * 从数据库中读取角色数据
	 *
	 * @param rid
	 * @return
	 */
	private RoleBO getRoleFromDB(Integer rid) {
		RoleEntity roleEntity = roleService.getById(rid);
		if (roleEntity == null) {
			return null;
		}

		RoleBO roleBO = new RoleBO();
		BeanUtils.copyProperties(roleEntity, roleBO);

		return roleBO;
	}

	/**
	 * 从数据库中读取角色数据
	 *
	 * @param userId
	 * @return
	 */
	private Integer getRoleFromDBByUserId(Integer userId) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setUid(userId);

		RoleEntity result = roleService.getOne(new QueryWrapper<>(roleEntity));

		if (result == null) {
			return null;
		}

		RoleBO roleBO = new RoleBO();
		BeanUtils.copyProperties(result, roleBO);

		// 缓存角色数据
		roleDataMap.putIfAbsent(userId, roleBO);

		return roleBO.getId();
	}

	/**
	 * 创建角色
	 *
	 * @param createDTO
	 * @return
	 */
	private RoleEntity createRoleToDB(CreateDTO createDTO) {
		RoleEntity roleEntity = new RoleEntity();
		BeanUtils.copyProperties(createDTO, roleEntity);

		if (roleService.getBaseMapper().insert(roleEntity) == 0) {
			return null;
		}

		// 缓存用户与角色的对应关系
		userRoleMap.putIfAbsent(createDTO.getUid(), roleEntity.getId());

		return roleEntity;
	}

	private <T> boolean updateRoleDataToDB(Integer rid, T roleData){
		RoleEntity roleEntity = new RoleEntity();

		roleEntity.setId(rid);

		BeanUtils.copyProperties(roleData, roleEntity);

		return roleService.update(roleEntity, new UpdateWrapper<>());
	}


	private boolean updateCityFacilityDataToDB(RoleBO roleBO){
		List<CityFacilityEntity> cityFacilityEntities = new ArrayList<>();
		roleBO.getCityFacilities().forEach((cityId, facilities) -> {
			CityFacilityEntity cityFacilityEntity = new CityFacilityEntity();
			cityFacilityEntity.setRid(roleBO.getId());
			cityFacilityEntity.setCityId(cityId);
			cityFacilityEntity.setFacilities(JSON.toJSONString(facilities));

			cityFacilityEntities.add(cityFacilityEntity);
		});

		return cityFacilityService.saveOrUpdateBatch(cityFacilityEntities);
	}
}
