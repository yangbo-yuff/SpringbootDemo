package com.yb.yff.game.business.businessDataMgr.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.constant.EnumUtils;
import com.yb.yff.game.data.constant.myEnum.BuildingOperationType;
import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.union.UnionDTO;
import com.yb.yff.game.data.entity.MapRoleBuildEntity;
import com.yb.yff.game.service.IMapRoleBuildService;
import com.yb.yff.game.service.IMapRoleCityService;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.data.constant.myEnum.BuildType;
import com.yb.yff.game.data.dto.city.NationalMap;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.nationMap.ConfigDTO;
import com.yb.yff.game.data.dto.nationMap.MapBuildDTO;
import com.yb.yff.game.data.dto.role.RoleResourceYideldDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 地图建筑物管理
 */
@Component
@Slf4j
public class BuildMgrImpl implements IJsonDataHandler {

	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	UnionMgrImpl unionMgr;

	@Autowired
	IMapRoleBuildService mapRoleBuildService;

	@Autowired
	IMapRoleCityService mapRoleCityService;

	// DB数据  key db  id
	ConcurrentHashMap<Integer, MapBuildDTO> dbRoleBildMap = new ConcurrentHashMap<>();

	// key : pos Id    CityPositionUtils.position2Number
	ConcurrentHashMap<Integer, MapBuildDTO> posRoleBildMap = new ConcurrentHashMap<>();

	//  key : role Id
	ConcurrentHashMap<Integer, List<MapBuildDTO>> roleRoleBildMap = new ConcurrentHashMap<>();

	// key:time  db id
	ConcurrentHashMap<Long, ConcurrentHashMap<Integer, MapBuildDTO>> giveUpRoleBildMap = new ConcurrentHashMap<>();

	/**
	 * 执行操作中的建筑物
	 * key:time  db id
	 */
	ConcurrentHashMap<Long, ConcurrentHashMap<Integer, MapBuildDTO>> inOperationBildMap = new ConcurrentHashMap<>();

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	@Transactional
	public void syncData2DB() {
		// 1 初始化缓存 DB
		initCache();

		// 2 恢复建筑的执行进程
		recoverPendingMap();
	}

	/**
	 * 恢复处理中的 giveUpRoleBildMap 和 destroyRoleBildMap
	 */
	public void recoverPendingMap(){
		getMapBuildMap().forEach((k, v) -> {
			Long giveUpTime = v.getGiveUp_time();
			if(giveUpTime != null && giveUpTime > 0){
				putGiveUpBuild(v);
			}

			Long endTime = v.getEnd_time();
			if(endTime != null && endTime > 0){
				putInOperationBild(v);
			}
		});
	}

	private void initCache(){
		if (dbRoleBildMap.size() > 0) {
			return;
		}

		// 查询数据库中是否有记录
		QueryWrapper<MapRoleBuildEntity> queryWrapper = new QueryWrapper<>();

		long count = mapRoleBuildService.count(queryWrapper);

		if (count > 0) {
			List<MapRoleBuildEntity> dbList = mapRoleBuildService.getBaseMapper().selectList(queryWrapper);
			dbList.forEach(mapRoleBuildEntity -> {
				MapBuildDTO mapRoleBuild = buildEntity2DTO(mapRoleBuildEntity);
				addMapBuildDTO2Cache(mapRoleBuild);
			});
			return;
		}

		// 同步 系统建筑到数据库
		List<NationalMap> nationlMapList = jsonConfigMgr.getNationalConfigMaps().getSysBuildList();

		List<MapRoleBuildEntity> mbEntityList = new ArrayList<>();
		List<MapBuildDTO> mbDTOList = new ArrayList<>();

		nationlMapList.forEach(nationlMap -> {
			MapBuildDTO mapRoleBuild = newMapBuildDTOByPos(nationlMap);
			mbDTOList.add(mapRoleBuild);

			MapRoleBuildEntity entity = buildDTO2Entity(mapRoleBuild);
			mbEntityList.add(entity);
		});

		// 每次提交500条记录，直到提交完毕
		mapRoleBuildService.saveOrUpdateBatch(mbEntityList, 500);

		// 处理 缓存
		for (int index = 0; index < mbEntityList.size(); index++) {

			MapBuildDTO mbDTO = mbDTOList.get(index);

			mbDTO.setId(mbEntityList.get(index).getId());

			addMapBuildDTO2Cache(mbDTO);
		}
	}

	private void addMapBuildDTO2Cache(MapBuildDTO mapBuildDTO) {
		dbRoleBildMap.put(mapBuildDTO.getId(), mapBuildDTO);

		posRoleBildMap.put(CityPositionUtils.position2Number(mapBuildDTO.getX(), mapBuildDTO.getY()), mapBuildDTO);

		roleRoleBildMap.computeIfAbsent(mapBuildDTO.getRid(), k -> new ArrayList<>()).add(mapBuildDTO);
	}

	MapBuildDTO buildEntity2DTO(MapRoleBuildEntity entity) {
		MapBuildDTO dto = newMapBuildDTOByConfig(entity.getType(), entity.getLevel());

		BeanUtils.copyProperties(entity, dto);

		dto.setRNick(roleDataMgr.getRoleNick(entity.getRid()));

		dto.setCur_durable(entity.getCurDurable());
		dto.setMax_durable(entity.getMaxDurable());
		dto.setOp_level(entity.getOpLevel());
		dto.setEnd_time(entity.getEndTime());
		dto.setOccupy_time(entity.getOccupyTime());
		dto.setGiveUp_time(entity.getGiveupTime());

		dto.setOperationType(EnumUtils.fromValue(BuildingOperationType.class, entity.getOperationType()));

		return dto;
	}

	MapRoleBuildEntity buildDTO2Entity(MapBuildDTO dto) {

		MapRoleBuildEntity entity = new MapRoleBuildEntity();

		BeanUtils.copyProperties(dto, entity);

		entity.setCurDurable(dto.getCur_durable());
		entity.setMaxDurable(dto.getMax_durable());
		entity.setOpLevel(dto.getOp_level());
		entity.setEndTime(dto.getEnd_time());
		entity.setOccupyTime(dto.getOccupy_time());
		entity.setGiveupTime(dto.getGiveUp_time());

		if(dto.getOperationType() != null){
			entity.setOperationType(dto.getOperationType().getValue());
		}


		return entity;
	}

	/**
	 * 根据配置创建一个MapBuildDTO对象
	 *
	 * @param type
	 * @param level
	 * @return
	 */
	MapBuildDTO newMapBuildDTOByConfig(Integer type, Integer level) {
		MapBuildDTO mapRoleBuild = new MapBuildDTO();
		mapRoleBuild.setRid(0);

		ConfigDTO buildConfig = jsonConfigMgr.getBuildConfigByTypeAndLevel(type, level);
		if (buildConfig == null) {
			log.info("====================  type:{}, level:{} nationlMap config is null!", type, level);
			return mapRoleBuild;
		}

		BeanUtils.copyProperties(buildConfig, mapRoleBuild);

		mapRoleBuild.setOp_level(buildConfig.getLevel());
		mapRoleBuild.setMax_durable(buildConfig.getDurable());
		mapRoleBuild.setCur_durable(buildConfig.getDurable());

		return mapRoleBuild;
	}

	/**
	 * 创建一个MapRoleBuildEntity对象
	 *
	 * @param nationlMap
	 * @return
	 */
	private MapBuildDTO newMapBuildDTOByPos(NationalMap nationlMap) {
		MapBuildDTO mapBuildDTO = newMapBuildDTOByConfig(nationlMap.getType(), nationlMap.getLevel());

		mapBuildDTO.setX(nationlMap.getPos().getX());
		mapBuildDTO.setY(nationlMap.getPos().getY());

		return mapBuildDTO;
	}

	/**
	 * 获取城市列表
	 *
	 * @param rid
	 * @return
	 */
	public List<MapBuildDTO> getRoleBuilds(Integer rid) {
		return roleRoleBildMap.computeIfAbsent(rid, k -> new ArrayList<>());
	}

	public List<BuildDTO> getRoleBuildDTOs(Integer rid) {
		List<BuildDTO> buildDTOs = new ArrayList<>();
		roleRoleBildMap.computeIfAbsent(rid, k -> new ArrayList<>()).forEach(mapRoleBuild -> {
			BuildDTO dto = MapBuild2BuildDTO(mapRoleBuild);
			buildDTOs.add(dto);
		});

		return buildDTOs;
	}

	public void addRoleBuilds(MapBuildDTO mapRoleBuild) {
		this.getRoleBuilds(mapRoleBuild.getRid()).add(mapRoleBuild);
	}

	public void delRoleBuilds(MapBuildDTO mapRoleBuild) {
		this.getRoleBuilds(mapRoleBuild.getRid()).remove(mapRoleBuild);
	}

	/**
	 * 获取 单元格配置数据
	 * @param posX, posY
	 */
	public NationalMap getCellConfigData(Integer posX, Integer posY) {
		int posId = CityPositionUtils.position2Number(posX, posY);
		return getCellConfigData(posId);
	}
	/**
	 * 获取 单元格配置数据
	 * @param posId
	 */
	public NationalMap getCellConfigData(Integer posId) {
		Map<Integer, NationalMap> nationlMapMap = jsonConfigMgr.getNationalConfigMaps().getNationalMapMap();
		return nationlMapMap.get(posId);
	}

	/**
	 * 获取角色领地的资源量产
	 *
	 * @param rid
	 * @return
	 */
	public RoleResourceYideldDTO getBuildResouerceYield(Integer rid) {
		RoleResourceYideldDTO roleResourceYideld = new RoleResourceYideldDTO(rid);

		List<MapBuildDTO> builds = getRoleBuilds(rid);
		if (builds == null || builds.size() == 0) {
			return null;
		}
		builds.stream().forEach(mapRoleBuildBO -> {
			ConfigDTO configDTO = jsonConfigMgr.getNationConfig().getNmcMap().get(mapRoleBuildBO.getType()).get(mapRoleBuildBO.getLevel());
			roleResourceYideld.setGrain_yield(roleResourceYideld.getGrain_yield() + configDTO.getGrain());
			roleResourceYideld.setIron_yield(roleResourceYideld.getIron_yield() + configDTO.getIron());
			roleResourceYideld.setStone_yield(roleResourceYideld.getStone_yield() + configDTO.getStone());
			roleResourceYideld.setWood_yield(roleResourceYideld.getWood_yield() + configDTO.getWood());
		});

		return roleResourceYideld;
	}



	public ConcurrentHashMap<Integer, MapBuildDTO> getMapBuildMap() {
		return dbRoleBildMap;
	}

	public MapBuildDTO getPositionBuild(PositionDTO pos) {
		int posId = CityPositionUtils.position2Number(pos);
		return posRoleBildMap.computeIfAbsent(posId, k -> createMapBuildFromConfig(posId));
	}

	public MapBuildDTO getPositionBuild(int x, int y) {
		int posId = CityPositionUtils.position2Number(x, y);
		return posRoleBildMap.computeIfAbsent(posId, k -> createMapBuildFromConfig(posId));
	}

	/**
	 * 获取 当前时间之前的 待执行操作的 城池
	 *
	 * @return
	 */
	public synchronized List<MapBuildDTO> getCurBeforeInOperationBilds() {

		Long curTime = System.currentTimeMillis();

		return getTimeBeforeInOperationBilds(curTime);
	}

	/**
	 * 获取 指定时间之前的 待执行操作的 城池
	 *
	 * @param endTime
	 * @return
	 */
	public synchronized List<MapBuildDTO> getTimeBeforeInOperationBilds(Long endTime) {
		List<MapBuildDTO> beforeMapList = new ArrayList<>();
		inOperationBildMap.entrySet().forEach(entry -> {
			if(entry.getKey() <= endTime){
				Map<Integer, MapBuildDTO> map = entry.getValue();
				beforeMapList.addAll(map.values());
			}
		});

		return beforeMapList;
	}

	public ConcurrentHashMap<Integer, MapBuildDTO> getInOperationBilds(Long endTime) {
		return inOperationBildMap.computeIfAbsent(endTime, k -> null);
	}

	public synchronized void delInOperationBilds(MapBuildDTO build) {
		if(build.getEnd_time() == null || build.getEnd_time() == 0){
			return;
		}

		ConcurrentHashMap<Integer, MapBuildDTO> map = this.getInOperationBilds(build.getEnd_time());
		if(map != null){
			map.computeIfPresent(build.getId(), (id, value) -> null);

			if (map.isEmpty()){
				delInOperationBildsByEndTime(build.getEnd_time());
			}
		}
	}

	public void delInOperationBildsByEndTime(Long endTime) {
		inOperationBildMap.computeIfPresent(endTime, (time, value) -> null);
	}

	public void putInOperationBild(MapBuildDTO build) {
		inOperationBildMap.computeIfAbsent(build.getEnd_time(), k -> new ConcurrentHashMap<>()).put(build.getId(), build);
	}

	/**
	 * 获取 当前时间之前的  待放弃的 城池
	 *
	 * @return
	 */
	public synchronized List<MapBuildDTO> getCurBeforeGiveUpBuilds() {

		Long curTime = System.currentTimeMillis();

		return getTimeBeforeGiveUpBuilds(curTime);
	}

	/**
	 * 获取 指定时间之前的 待放弃的 城池
	 * @param giveUpTime
	 * @return
	 */
	public synchronized List<MapBuildDTO> getTimeBeforeGiveUpBuilds(Long giveUpTime) {
		List<MapBuildDTO> beforeMapList = new ArrayList<>();
		giveUpRoleBildMap.entrySet().forEach(entry -> {
			if(entry.getKey() < giveUpTime){
				Map<Integer, MapBuildDTO> map = entry.getValue();
				beforeMapList.addAll(map.values());
			}
		});

		return beforeMapList;
	}

	/**
	 * 获取 指定时间之前的 待放弃的 城池
	 * @param giveUpTime
	 * @return
	 */
	public ConcurrentHashMap<Integer, MapBuildDTO> getGiveUpBuild(Long giveUpTime) {
		return giveUpRoleBildMap.computeIfAbsent(giveUpTime, k -> null);
	}

	/**
	 * 删除 放弃的城池
	 * @param build
	 */
	public synchronized void delGiveUpBuild(MapBuildDTO build) {
		if(build.getGiveUp_time() == null || build.getGiveUp_time() == 0){
			return;
		}

		ConcurrentHashMap<Integer, MapBuildDTO> map = this.getGiveUpBuild(build.getGiveUp_time());
		if(map != null){
			map.computeIfPresent(build.getId(), (id, value) -> null);

			if (map.isEmpty()){
				delGiveUpByEndTime(build.getGiveUp_time());
			}
		}
	}

	public void delGiveUpByEndTime(Long giveUpTime) {
		giveUpRoleBildMap.computeIfPresent(giveUpTime, (time, value) -> null);
	}

	public void putGiveUpBuild(MapBuildDTO build) {
		giveUpRoleBildMap.computeIfAbsent(build.getGiveUp_time(), k -> new ConcurrentHashMap<>()).put(build.getId(), build);
	}

	/**
	 * 定时处理empty的 giveUpRoleBildMap 和 destroyRoleBildMap
	 */
	public void checkPendingMapEmpty(){
		giveUpRoleBildMap.forEach((time, map) -> {
			// 移除值为 null 的键值对
			map.entrySet().removeIf(entry -> entry.getValue() == null);

			// 如果 map 为空，则从 giveUpRoleBildMap 中移除
			if (map.isEmpty()) {
				giveUpRoleBildMap.remove(time);
			}
		});

		inOperationBildMap.forEach((time, map) -> {
			// 移除值为 null 的键值对
			map.entrySet().removeIf(entry -> entry.getValue() == null);

			// 如果 map 为空，则从 destroyRoleBildMap 中移除
			if (map.isEmpty()) {
				inOperationBildMap.remove(time);
			}
		});
	}


	public Integer roleFortressCnt(Integer rid) {

		List<MapBuildDTO> mrbList = roleRoleBildMap.computeIfAbsent(rid, k -> new ArrayList<>());
		if (mrbList == null || mrbList.size() == 0) {
			return 0;
		}

		Long count = mrbList.stream().filter(b -> b.getType() == BuildType.MapBuildFortress.getValue()).count();
		return count.intValue();
	}

	/**
	 * 是否是免战状态
	 *
	 * @param buildDTO
	 * @return
	 */
	public synchronized boolean isWarFree(MapBuildDTO buildDTO) {
		if (buildDTO.getOccupy_time() == null) {
			return false;
		}

		Long timeInterval = System.currentTimeMillis() / 1000 - buildDTO.getOccupy_time();
		if (timeInterval < jsonConfigMgr.getBasicConfig().getBuild().getWar_free()) {
			return true;
		} else {
			return false;
		}
	}

	//是否有修改等级权限
	public boolean isHaveModifyLVAuth(MapBuildDTO mapRoleBuildBO) {
		return mapRoleBuildBO.getType() == BuildType.MapBuildFortress.getValue();
	}

	public boolean isBusy(MapBuildDTO mapRoleBuildBO) {
		return mapRoleBuildBO.getLevel() != mapRoleBuildBO.getOp_level();
	}

	/**
	 * 是否资源产地
	 * @param mapRoleBuildBO
	 * @return
	 */
	public boolean isResBuild(MapBuildDTO mapRoleBuildBO) {
		return (mapRoleBuildBO.getGrain() != null && mapRoleBuildBO.getGrain() > 0)
				|| (mapRoleBuildBO.getStone() != null && mapRoleBuildBO.getStone() > 0)
				|| (mapRoleBuildBO.getIron() != null && mapRoleBuildBO.getIron() > 0)
				|| (mapRoleBuildBO.getWood() != null && mapRoleBuildBO.getWood() > 0);
	}

	/**
	 * 是否有调兵权限
	 * @param build
	 * @return
	 */
	public boolean isHasTransferAuth(MapBuildDTO build) {
		return build.getType() == BuildType.MapBuildFortress.getValue() || build.getType() == BuildType.MapBuildSysFortress.getValue();
	}

	/**
	 * 是否在处理放弃过程中
	 * @param build
	 * @return
	 */
	public boolean isInGiveUp(MapBuildDTO build) {
		return build.getGiveUp_time() != null && build.getGiveUp_time() != 0;
	}

	public Integer buildCnt(Integer rid) {
		List<MapBuildDTO> bs = getRoleBuilds(rid);

		return bs.size();
	}

	public void reset(MapBuildDTO build) {
		NationalMap cellConfig =  getCellConfigData(build.getX(), build.getY());
		ConfigDTO buildConfig = jsonConfigMgr.getBuildConfigByTypeAndLevel(cellConfig.getType(), cellConfig.getLevel());
		if (buildConfig != null) {
			BeanUtils.copyProperties(buildConfig, build);
		}


		build.setGiveUp_time(0l);
		build.setRid(0);
		build.setEnd_time(0l);
		build.setOp_level(cellConfig.getLevel());
		build.setCur_durable(Math.min(build.getMax_durable(), build.getCur_durable()));
	}

	public synchronized boolean isCanBuild(PositionDTO pos) {
		return isCanBuild(pos.getX(), pos.getY());
	}

	public synchronized boolean isCanBuild(Integer posX, Integer posY) {
		MapBuildDTO mapRoleBuild = getPositionBuild(posX, posY);

		if (mapRoleBuild != null) {
			if (mapRoleBuild.getType() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public void convertToRes(MapBuildDTO build) {
		clearOperationState(build);

		Integer rid = build.getRid();
		Long giveUpTime = build.getGiveUp_time();
		reset(build);
		build.setRid(rid);
		build.setGiveUp_time(giveUpTime);
	}

	/**
	 * 完成操作后， 清理操作状态
	 * @param build
	 */
	public void clearOperationState(MapBuildDTO build) {
		build.setEnd_time(0l);
		build.setOperationType(BuildingOperationType.BUILD_NOTHING);
		build.setLevel(build.getOp_level());
	}

	private synchronized MapBuildDTO createMapBuildFromConfig(Integer posKey) {
		Map<Integer, NationalMap> nationalMapResMap = jsonConfigMgr.getNationalConfigMaps().getNationalMapMap();
		if (nationalMapResMap == null) {
			return null;
		}

		NationalMap nationlMap = nationalMapResMap.get(posKey);
		if (nationlMap == null || nationlMap.getType() == 0 || nationlMap.getLevel() == 0) {
			return null;
		}


		MapBuildDTO mapRoleBuild = newMapBuildDTOByPos(nationlMap);

		// 新建 build 无法确定是否需要上传DB, 故暂时不保存
//		MapRoleBuildEntity mapRoleBuildEntity = buildDTO2Entity(mapRoleBuild);
//		if (!mapRoleBuildService.save(mapRoleBuildEntity)) {
//			log.error("========== 创建一个地图单元格数据时，保存DB失败");
//		}
//
//		mapRoleBuild.setId(mapRoleBuildEntity.getId());

		//  新建时，因为不归属任何角色，故不需要添加到dbRoleBildMap
//		dbRoleBildMap.put(mapRoleBuild.getId(), mapRoleBuild);

		// 新建时，因为不归属任何角色，故不需要添加到角色列表
//		roleRoleBildMap.computeIfAbsent(mapRoleBuild.getRid(), k -> new ArrayList<>()).add(mapRoleBuild);

		return mapRoleBuild;
	}

	/**
	 * 更新地图数据到DB
	 *
	 * @param mapRoleBuild
	 * @return
	 */
	public boolean updateBuild2DB(MapBuildDTO mapRoleBuild) {
		MapRoleBuildEntity mapRoleBuildEntity = buildDTO2Entity(mapRoleBuild);

		UpdateWrapper<MapRoleBuildEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", mapRoleBuild.getId());

		return mapRoleBuildService.update(mapRoleBuildEntity, updateWrapper);
	}


	public BuildDTO MapBuild2BuildDTO(MapBuildDTO mapBuild) {
		BuildDTO buildDTO = new BuildDTO();
		BeanUtils.copyProperties(mapBuild, buildDTO);

		RoleDTO role = roleDataMgr.getRoleDTO(mapBuild.getRid());

		buildDTO.setParent_id(role.getParentId());

		UnionDTO union = unionMgr.getUnion(role.getUnionId());
		if (union != null) {

			buildDTO.setUnion_id(union.getId());
			// TODO 联盟名称
			buildDTO.setUnion_name(union.getName());
		}

		return buildDTO;
	}

	public synchronized void removeBuildFromRole(Integer posX, Integer posY) {
		MapBuildDTO build = getPositionBuild(posX, posY);

		if (build == null) {
			return;
		}

		if (build.getRid() != 0) {
			delRoleBuilds(build);
		}

		//移除放弃事件
		delGiveUpBuild(build);

		//移除拆除事件
		delInOperationBilds(build);

		reset(build);
	}


	public synchronized boolean addBuild2Role(Integer rid, Integer posX, Integer posY) {
		MapBuildDTO rb = getPositionBuild(posX, posY);

		if (rb == null) {
			return false;
		}

		// 移除旧主
		if (rb.getRid() != 0) {
			delRoleBuilds(rb);
		}

		// 归属新主
		rb.setRid(rid);
		rb.setOccupy_time(System.currentTimeMillis());
		addRoleBuilds(rb);

		return true;
	}
}
