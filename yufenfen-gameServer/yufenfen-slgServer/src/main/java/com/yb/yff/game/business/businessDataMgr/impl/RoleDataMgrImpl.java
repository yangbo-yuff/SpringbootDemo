package com.yb.yff.game.business.businessDataMgr.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.role.CreateDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.role.RoleResourceData;
import com.yb.yff.game.data.dto.role.RoleResourceYideldDTO;
import com.yb.yff.game.data.entity.RoleEntity;
import com.yb.yff.game.jsondb.data.dto.BasicConscript;
import com.yb.yff.game.jsondb.data.dto.BasicRole;
import com.yb.yff.game.service.IRoleService;
import com.yb.yff.game.utils.CityPositionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class RoleDataMgrImpl implements IJsonDataHandler {
	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	IRoleService roleService;

	@Autowired
	BuildMgrImpl buildMgrImpl;

	/**
	 * 用户id -> 角色数据缓存
	 * <userId,RoleDTO>
	 */
	private ConcurrentHashMap<Integer, RoleDTO> userRoleMap = new ConcurrentHashMap<>();

	/**
	 * rid -> 角色数据缓存
	 * <rid, RoleDTO>
	 */
	private ConcurrentHashMap<Integer, RoleDTO> roleMap = new ConcurrentHashMap<>();

	/**
	 * 角色数据缓存
	 * <rid,RoleResourceData>
	 */
	private ConcurrentHashMap<Integer, RoleResourceData> roleResourceMap = new ConcurrentHashMap<>();

	/**
	 * 以position为中心位置的角色列表
	 */
	private ConcurrentHashMap<Integer, List<Integer>> rolesCenterPosition = new ConcurrentHashMap<>();


	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {
		if (userRoleMap.size() > 0) {
			return;
		}

		List<RoleEntity> dbList = roleService.getBaseMapper().selectList(null);
		if (dbList.size() == 0) {
			return;
		}

		dbList.stream().forEach(roleEntity -> {
			// 插入缓存
			updateRoleCache(roleEntity);
		});
	}

	private RoleResourceData createRoleResourceData(RoleDTO role) {
		RoleResourceData roleResourceData = new RoleResourceData();
		BeanUtils.copyProperties(role, roleResourceData);
		roleResourceData.setRid(role.getRid());

		roleResourceData.setDepot_capacity(getRoleConfig().getDepot_capacity());

		updateResourceYield(roleResourceData);

		return roleResourceData;
	}

	/**
	 * 计算产量
	 *
	 * @param roleResourceData
	 */
	private void updateResourceYield(RoleResourceData roleResourceData) {
		// 计算 占领土地的产量
		RoleResourceYideldDTO roleBuildYideld = buildMgrImpl.getBuildResouerceYield(roleResourceData.getRid());
		if (roleBuildYideld == null) {
			roleBuildYideld = new RoleResourceYideldDTO(roleResourceData.getRid());
		}
		BasicRole basicRoleConfig = getRoleConfig();

		// 合计 产量
		roleResourceData.setIron_yield(roleBuildYideld.getIron_yield() + basicRoleConfig.getIron_yield());
		roleResourceData.setWood_yield(roleBuildYideld.getWood_yield() + basicRoleConfig.getWood_yield());
		roleResourceData.setGrain_yield(roleBuildYideld.getGrain_yield() + basicRoleConfig.getGrain_yield());
		roleResourceData.setStone_yield(roleBuildYideld.getStone_yield() + basicRoleConfig.getStone_yield());
		roleResourceData.setGold_yield(roleBuildYideld.getGold_yield() + basicRoleConfig.getGold_yield());
	}

	/**
	 * 获取角色资源数据，不存在则创建默认值
	 *
	 * @param rid
	 * @return
	 */
	public RoleResourceData getRoleResourceData(Integer rid) {
		return roleResourceMap.get(rid);
	}

	/**
	 * 获取角色资源数据，
	 *
	 * @return
	 */
	public Map<Integer, RoleResourceData> getRoleResources() {
		return roleResourceMap;
	}

	/**
	 * 获取玩家角色配置
	 *
	 * @return
	 */
	public BasicRole getRoleConfig() {
		return jsonConfigMgr.getBasicConfig().getRole();
	}

	/**
	 * 获取征兵消耗配置
	 *
	 * @return
	 */
	public BasicConscript getConscriptConfig() {
		return jsonConfigMgr.getBasicConfig().getConscript();
	}

	/**
	 * 读取用户角色数据
	 *
	 * @param userId
	 * @return
	 */
	public RoleDTO getRoleByUid(Integer userId) {
		return userRoleMap.get(userId);
	}

	/**
	 * 获取角色数据
	 *
	 * @param rid
	 * @return
	 */
	public RoleDTO getRoleDTO(Integer rid) {
		return roleMap.get(rid);
	}

	/**
	 * 设置玩家联盟
	 *
	 * @param rid
	 * @return
	 */
	public void setRoleUnion(Integer rid, Integer unionId) {
		roleMap.get(rid).setUnionId(unionId);
	}

	/**
	 * 玩家昵称
	 * @param rid
	 * @return
	 */
	public String getRoleNick(Integer rid){
		return Optional.ofNullable(roleMap.get(rid))
				.map(RoleDTO::getNickName)
				.orElse(null);
	}

	/**
	 * 创建角色
	 *
	 * @param createDTO
	 * @return
	 */
	public RoleEntity createRoleData(CreateDTO createDTO) {
		RoleEntity roleEntity = createRoleToDB(createDTO);
		if (roleEntity == null) {
			return null;
		}

		// 插入缓存
		updateRoleCache(roleEntity);

		return roleEntity;
	}

	private void updateRoleCache(RoleEntity roleEntity) {
		RoleDTO role = roleEntityToDTO(roleEntity);

		roleMap.put(role.getRid(), role);

		userRoleMap.put(role.getUid(), role);

		roleResourceMap.put(role.getRid(), createRoleResourceData(role));
	}

	private RoleDTO roleEntityToDTO(RoleEntity entity) {
		RoleDTO dto = new RoleDTO();
		BeanUtils.copyProperties(entity, dto);

		dto.setRid(entity.getId());

		return dto;
	}

	private Integer productIndex = 1;

	/**
	 * 按产量生成资源
	 */
	public void productResourceByYield(Integer multiple) {
		roleResourceMap.forEach((rid, roleResource) -> {
			Integer capacity = roleResource.getDepot_capacity();

			if (roleResource.getGold() < capacity) {
				Integer curGold = roleResource.getGold() + roleResource.getGold_yield() * multiple;
				roleResource.setGold(curGold > capacity ? capacity : curGold);
			}

			if (roleResource.getGrain() < capacity) {
				Integer curGrain = roleResource.getGrain() + roleResource.getGrain_yield() * multiple;
				roleResource.setGrain(curGrain > capacity ? capacity : curGrain);
			}

			if (roleResource.getIron() < capacity) {
				Integer curIron = roleResource.getIron() + roleResource.getIron_yield() * multiple;
				roleResource.setIron(curIron > capacity ? capacity : curIron);
			}

			if (roleResource.getStone() < capacity) {
				Integer curStone = roleResource.getStone() + roleResource.getStone_yield() * multiple;
				roleResource.setStone(curStone > capacity ? capacity : curStone);
			}

			if (roleResource.getWood() < capacity) {
				Integer curWood = roleResource.getWood() + roleResource.getWood_yield() * multiple;
				roleResource.setWood(curWood > capacity ? capacity : curWood);
			}

			// 政令不受倍数影响
			if (productIndex % 12 == 0) {
				if (roleResource.getDecree() < getRoleConfig().getDecree_limit()) {
					roleResource.setDecree(roleResource.getDecree() + 1);
				}

				productIndex = 1;
			}

			productIndex++;
		});

		updateResource2Cache();
	}

	private void updateResource2Cache() {
		roleResourceMap.forEach((rid, roleResource) -> {
			// 2 cache
			RoleDTO role = roleMap.get(rid);
			BeanUtils.copyProperties(roleResource, role);

			// 2 DB
			RoleDTO updateRole = new RoleDTO();
			BeanUtils.copyProperties(roleResource, updateRole);
			updateRoleDataToDB(updateRole);
		});
	}

	/**
	 * 根据中心坐标获取角色列表
	 *
	 * @param position
	 */
	public List<Integer> getRolesByPosition(PositionDTO position) {
		Integer posNum = CityPositionUtils.position2Number(position);
		return rolesCenterPosition.computeIfAbsent(posNum, k -> new ArrayList<>());
	}

	/**
	 * 更新产量
	 *
	 * @param rid
	 */
	public void updateRoleRessourceYield(Integer rid) {
		RoleResourceData roleResourceData = roleResourceMap.get(rid);
		if (roleResourceData == null) {
			RoleDTO role = getRoleDTO(rid);
			roleResourceMap.put(roleResourceData.getRid(), createRoleResourceData(role));
		}

		updateResourceYield(roleResourceData);
	}

	/**
	 * 更新中心坐标内的角色
	 *
	 * @param position
	 * @param role
	 * @return
	 */
	public boolean updatePositionRole2Cache(PositionDTO position, Integer role) {
		Integer posNum = CityPositionUtils.position2Number(position);

		rolesCenterPosition.computeIfAbsent(posNum, k -> new ArrayList<>()).add(role);

		return true;
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

		// 插入初始配置
		BeanUtils.copyProperties(getRoleConfig(), roleEntity);

		if (roleService.getBaseMapper().insert(roleEntity) == 0) {
			return null;
		}

		return roleEntity;
	}

	/**
	 * 更新角色数据到数据库,
	 * 注意传入的updateRole仅包含需要更新的部分字段 和 rid
	 *
	 * @param updateRole
	 * @return
	 */
	public boolean updateRoleDataToDB(RoleDTO updateRole) {
		RoleEntity roleEntity = new RoleEntity();

		roleEntity.setId(updateRole.getRid());

		BeanUtils.copyProperties(updateRole, roleEntity);

		UpdateWrapper updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", updateRole.getRid());

		return roleService.update(roleEntity, updateWrapper);
	}

	public boolean decreeIsEnough(Integer rid, Integer cost) {
		RoleResourceData resourceData = getRoleResourceData(rid);


		if (resourceData != null) {
			if (resourceData.getDecree() >= cost) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean tryUseDecree(Integer rid, Integer decree) {

		RoleResourceData resourceData = getRoleResourceData(rid);
		if (resourceData != null) {
			if (resourceData.getDecree() >= decree) {
				resourceData.setDecree(resourceData.getDecree() - decree);
				// TODO update DB
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean hasUnion(Integer rid){
		RoleDTO role = getRoleDTO(rid);

		return role.getUnionId() != null && role.getUnionId() > 0;
	}
}
