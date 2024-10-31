package com.yb.yff.game.service;

import com.yb.yff.game.data.entity.RoleEntity;
import com.yb.yff.sb.data.bo.FacilityBO;
import com.yb.yff.sb.data.bo.RoleBO;
import com.yb.yff.sb.data.dto.city.CityDTO;
import com.yb.yff.sb.data.dto.role.CreateDTO;
import com.yb.yff.sb.data.dto.role.RoleData;

import java.util.List;

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
 * @Class: GameDataManager
 * @CreatedOn 2024/10/26.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏数据管理
 */
public interface IGameRoleDataManager {
	/**
	 * 读取JSON配置
	 * @param clazz
	 * @return
	 */
	<T> T getJsonConfig(Class<T> clazz);

	/**
	 * 从数据库中读取角色ID
	 * @param userId
	 * @return
	 */
	Integer getRoleId(Integer userId);

	/**
	 * 获取角色数据
	 * @param userId
	 * @return
	 */
	<T extends RoleData> T getRoleDataByUserId(Integer userId, Class<T> clazz);

	/**
	 * 获取角色全部数据
	 * @param rid
	 * @return
	 */
	RoleBO getRoleVO(Integer rid);

	/**
	 * 获取角色数据
	 * @param rid
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T extends RoleData> T getRoleData(Integer rid, Class<T> clazz);

	/**
	 * 获取全部角色指定类型的数据
	 * @param rid
	 * @param clazz
	 * @return
	 * @param <T>
	 */
	<T extends RoleData> List<T> getAllRoleData(Integer rid, Class<T> clazz);

	/**
	 * 更新角色数据,
	 * 产量数据没有持久化，故只更新缓存
	 * @param roleData
	 * @param isUpdateDB
	 * @param <T>
	 */
	<T extends RoleData> boolean updateRoleData(T roleData, boolean isUpdateDB);

	/**
	 * 创建角色
	 * @param createDTO
	 * @return
	 */
	RoleEntity createRoleData(CreateDTO createDTO);

	/**
	 * 创建角色主城
	 * @param cityDTO
	 * @return
	 */
	boolean createRoleCityData(CityDTO cityDTO);

	/**
	 * 创建主城设施
	 * @param cityId
	 * @return
	 */
	List<FacilityBO> createCityFacilitiesData(Integer cityId);
}
