package com.yb.yff.game.business.businessLogic.impl.map;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.BuildMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessLogic.IArmyLogic;
import com.yb.yff.game.business.businessLogic.INationMapLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.constant.myEnum.BuildType;
import com.yb.yff.game.data.constant.myEnum.BuildingOperationType;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.nationMap.*;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigDTO;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigLevelDTO;
import com.yb.yff.game.data.dto.nationMap.config.NationConfigDTO;
import com.yb.yff.game.jsondb.data.dto.BasicBuild;
import com.yb.yff.game.service.DelayedTask.ITaskExecutionProcessListener;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.sb.config.AsyncThreadPoolConfig;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
 * @Class: getNationalMapConfig
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 获取区域地图配置
 */
@Service
@Slf4j
public class NationMapLogicImpl extends BusinessDataSyncImpl<MapBuildDTO> implements INationMapLogic, ITaskExecutionProcessListener {
	final IRoleLogic roleLogic;

	final RoleDataMgrImpl roleDataMgr;

	final JsonConfigMgr jsonConfigMgr;

	final BuildMgrImpl buildMgrImpl;

	final CityMgrImpl cityMgrImpl;

	final IPushService pushService;

	final IArmyLogic armyLogic;

	@Autowired
	public NationMapLogicImpl(IRoleLogic roleLogic, RoleDataMgrImpl roleDataMgr, JsonConfigMgr jsonConfigMgr,
	                          BuildMgrImpl buildMgrImpl, CityMgrImpl cityMgrImpl, IPushService pushService,
	                          IArmyLogic armyLogic){
		this.roleLogic = roleLogic;
		this.roleDataMgr = roleDataMgr;
		this.jsonConfigMgr = jsonConfigMgr;
		this.buildMgrImpl = buildMgrImpl;
		this.cityMgrImpl = cityMgrImpl;
		this.pushService = pushService;
		this.armyLogic = armyLogic;
	}

	@PostConstruct
	public void init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_roleBuild, pushService);
	}

	/**
	 * 获取区域地图配置
	 *
	 * @return
	 */
	@Override
	public List<ConfigDTO> getNationalMapConfig() {
		NationConfigDTO nationConfigDTO = jsonConfigMgr.getNationConfig();
		return nationConfigDTO.getCfg();
	}

	/**
	 * 给定位置范围内的建筑物列表
	 *
	 * @param x        X坐标
	 * @param y        Y坐标
	 * @param boundary 边界
	 * @return
	 */
	@Override
	public <T extends MapCellBaseData> List<T> scan(int x, int y, int boundary, Class<T> clazz) {
		if (x < 0 || x >= CityPositionUtils.MapWidth || y < 0 || y >= CityPositionUtils.MapHeight) {
			return Collections.emptyList();
		}

		int minX = Math.max(0, x - CityPositionUtils.ScanWidth);
		int maxX = Math.min(boundary, x + CityPositionUtils.ScanWidth);
		int minY = Math.max(0, y - CityPositionUtils.ScanHeight);
		int maxY = Math.min(boundary, y + CityPositionUtils.ScanHeight);

		List<T> roleBuilds = new ArrayList<>();

		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				T v = getPosBuildData(i, j, clazz);
				if (v != null && v.getRid() != 0) {
					roleBuilds.add(v);
				}
			}
		}
		return roleBuilds;
	}

	/**
	 * 给定位置并指定范围和方向内的建筑物列表
	 *
	 * @param x
	 * @param y
	 * @param length
	 * @return
	 */
	@Override
	public <T extends MapCellBaseData> List<T> scanBlock(int x, int y, int length, Class<T> clazz) {
		if (x < 0 || x >= CityPositionUtils.MapWidth || y < 0 || y >= CityPositionUtils.MapHeight) {
			return Collections.emptyList();
		}

		int maxX = Math.min(CityPositionUtils.MapWidth, x + length - 1);
		int maxY = Math.min(CityPositionUtils.MapHeight, y + length - 1);

		List<T> rb = new ArrayList<>();

		for (int i = x; i <= maxX; i++) {
			for (int j = y; j <= maxY; j++) {
				T v = getPosBuildData(i, j, clazz);
				if (v != null && (v.getRid() != 0 || v.getType() == null || v.getType() == BuildType.MapBuildSysCity.getValue() ||
						v.getType() == BuildType.MapBuildSysFortress.getValue())) {
					rb.add(v);
				}
			}
		}
		return rb;
	}

	/**
	 * @param x
	 * @param y
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	private <T extends MapCellBaseData> T getPosBuildData(Integer x, Integer y, Class<T> clazz) {
		if (clazz == BuildDTO.class) {
			return (T) Optional.ofNullable(buildMgrImpl.getPositionBuild(x, y)).orElse(null);
		} else if (clazz == CityDTO.class) {
			return (T) Optional.ofNullable(cityMgrImpl.getPositionCity(x, y)).orElse(null);
		} else {
			throw new IllegalArgumentException("不支持的类: " + clazz.getName());
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	@Override
	public ResponseCode giveUp(int rid, int x, int y) {
		MapBuildDTO b = buildMgrImpl.getPositionBuild(x, y);
		if (b == null) {
			return NetResponseCodeConstants.CannotGiveUp;
		}

		BasicBuild buildConfig = jsonConfigMgr.getBasicConfig().getBuild();

		if (buildMgrImpl.isWarFree(b)) {
			return NetResponseCodeConstants.BuildWarFree;
		}

		if (b.getGiveUpTime() != null && b.getGiveUpTime() > 0) {
			return NetResponseCodeConstants.BuildGiveUpAlready;
		}
		Long giveupTime = System.currentTimeMillis() + buildConfig.getGiveUp_time() * 1000;
		b.setGiveUpTime(giveupTime);

		syncExecute(b.getRid(), b);

		buildMgrImpl.putGiveUpBuild(b);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 创建建筑
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	@Override
	public ResponseCode build(MapCellReqDTO buildPos, Integer rid) {
		PositionDTO pos = new PositionDTO(buildPos.getX(), buildPos.getY());


		ResponseCode checkResult = checkMapBuild(rid, buildPos.getX(), buildPos.getY(), BuildingOperationType.BUILD_NEW);
		if (checkResult != NetResponseCodeConstants.SUCCESS) {
			return checkResult;
		}

		// 暂时不限制建筑数量
//		Basic basicConf = jsonConfigMgr.getBasicConfig();
//		int cnt = buildMgrImpl.roleFortressCnt(rid);
//		if (cnt >= basicConf.getBuild().getFortress_limit()) {
//			return NetResponseCodeConstants.CanNotBuildNew;
//		}

		return tryBuildOrUp(rid, buildPos.getType(), buildPos.getX(), buildPos.getY(), BuildingOperationType.BUILD_NEW);
	}

	/**
	 * 建筑升级
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<BuildDTO> upBbuild(MapBuildUpDTO buildPos, Integer rid) {
		LogicTaskResultDTO<BuildDTO> result = new LogicTaskResultDTO<>();

		ResponseCode checkResult = checkMapBuild(rid, buildPos.getX(), buildPos.getY(), BuildingOperationType.BUILD_UP);
		if (checkResult != NetResponseCodeConstants.SUCCESS) {
			result.setCode(checkResult);
			return result;
		}

		ResponseCode tryResult = tryBuildOrUp(rid, null, buildPos.getX(), buildPos.getY(), BuildingOperationType.BUILD_UP);
		result.setCode(tryResult);

		MapBuildDTO mapBuild = buildMgrImpl.getPositionBuild(buildPos.getX(), buildPos.getY());
		if (mapBuild == null) {
			result.setCode(NetResponseCodeConstants.BuildNotMe);
			return result;
		}

		result.setCode(tryResult);

		BuildDTO buildDTO = buildMgrImpl.MapBuild2BuildDTO(mapBuild);
		result.setResult(buildDTO);

		return result;
	}


	/**
	 * 删除
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<BuildDTO> delBbuild(MapBuildUpDTO buildPos, Integer rid) {
		LogicTaskResultDTO<BuildDTO> result = new LogicTaskResultDTO<>();

		ResponseCode checkResult = checkMapBuild(rid, buildPos.getX(), buildPos.getY(), BuildingOperationType.BUILD_DEL);
		if (checkResult != NetResponseCodeConstants.SUCCESS) {
			result.setCode(checkResult);
			return result;
		}

		MapBuildDTO mapBuild = buildMgrImpl.getPositionBuild(buildPos.getX(), buildPos.getY());
		if (mapBuild == null) {
			result.setCode(NetResponseCodeConstants.BuildNotMe);
			return result;
		}

		ResponseCode tryResult = destroy(mapBuild);

		result.setCode(tryResult);

		BuildDTO buildDTO = buildMgrImpl.MapBuild2BuildDTO(mapBuild);
		result.setResult(buildDTO);

		return result;
	}

	private synchronized ResponseCode destroy(MapBuildDTO mapBuildDTO) {

		MBCustomConfigLevelDTO cfg = jsonConfigMgr.getNationArmyConfig(mapBuildDTO.getType(), mapBuildDTO.getLevel());
		if (cfg == null) {
			return NetResponseCodeConstants.InvalidParam;
		}

		// TODO 迷惑，为什么删除建筑要扣各种资源
//		ResponseCode code = roleLogic.checkUserNeed(b.getRid(), cfg.getNeed());
//		if (code != NetResponseCodeConstants.SUCCESS) {
//			return code;
//		}

		Long endTime = System.currentTimeMillis() + cfg.getTime() * 1000;

		mapBuildDTO.setEndTime(endTime);
		mapBuildDTO.setOperationType(BuildingOperationType.BUILD_DEL);

		buildMgrImpl.putInOperationBild(mapBuildDTO);

		mapBuildDTO.setOpLevel(0);

		syncExecute(mapBuildDTO.getRid(), mapBuildDTO);

		return NetResponseCodeConstants.SUCCESS;
	}

	private synchronized ResponseCode checkMapBuild(Integer rid, Integer x, Integer y, BuildingOperationType operationType) {

		if (!buildIsRId(rid, x, y)) {
			return NetResponseCodeConstants.BuildNotMe;
		}

		MapBuildDTO buildDTO = buildMgrImpl.getPositionBuild(x, y);

		switch (operationType) {
			case BUILD_NEW:
				if (!buildMgrImpl.isResBuild(buildDTO) || buildMgrImpl.isBusy(buildDTO)) {
					return NetResponseCodeConstants.CanNotBuildNew;
				}
				break;
			case BUILD_UP:
				if (!buildMgrImpl.isHaveModifyLVAuth(buildDTO) || buildMgrImpl.isInGiveUp(buildDTO) || buildMgrImpl.isBusy(buildDTO)) {
					return NetResponseCodeConstants.CanNotUpBuild;
				}
				break;
			case BUILD_DEL:
				if (!buildMgrImpl.isHaveModifyLVAuth(buildDTO) || buildMgrImpl.isInGiveUp(buildDTO) || buildMgrImpl.isBusy(buildDTO)) {
					return NetResponseCodeConstants.CanNotDestroy;
				}
				break;
			default:
				break;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 判断是否是当前角色的
	 *
	 * @param x
	 * @param y
	 * @param rid
	 * @return
	 */
	@Override
	public boolean buildIsRId(Integer rid, Integer x, Integer y) {
		MapBuildDTO build = buildMgrImpl.getPositionBuild(x, y);

		if (build == null || build.getRid() == null || build.getRid() != rid) {
			return false;
		}

		return true;
	}

	/**
	 * 升级或新建
	 *
	 * @param rid
	 * @param confType
	 * @param x
	 * @param y
	 * @param operationType
	 */
	private ResponseCode tryBuildOrUp(Integer rid, Integer confType, Integer x, Integer y, BuildingOperationType operationType) {
		MapBuildDTO mapRoleBuild = buildMgrImpl.getPositionBuild(x, y);
		if (mapRoleBuild == null) {
			return NetResponseCodeConstants.BuildNotMe;
		}

		MBCustomConfigDTO mbCustomConfigDTO = jsonConfigMgr.getMBCustomConfig(confType == null ? mapRoleBuild.getType() : confType);

		if (mbCustomConfigDTO == null) {
			return NetResponseCodeConstants.InvalidParam;
		}

		int level = operationType == BuildingOperationType.BUILD_NEW ? 1 : mapRoleBuild.getLevel() + 1;

		MBCustomConfigLevelDTO cfg = mbCustomConfigDTO.getLevelMap().get(level);
		if (cfg == null) {
			return NetResponseCodeConstants.InvalidParam;
		}

		ResponseCode checkResult = roleLogic.checkUserNeed(rid, cfg.getNeed());
		if (checkResult != NetResponseCodeConstants.SUCCESS) {
			return checkResult;
		}

		mapRoleBuild.setType(mbCustomConfigDTO.getType());
		mapRoleBuild.setName(mbCustomConfigDTO.getName());
		mapRoleBuild.setLevel(cfg.getLevel() - 1);
		mapRoleBuild.setOpLevel(cfg.getLevel());
		mapRoleBuild.setGiveUpTime(null);

		mapRoleBuild.setWood(0);
		mapRoleBuild.setIron(0);
		mapRoleBuild.setStone(0);
		mapRoleBuild.setGrain(0);
		Long endTime = System.currentTimeMillis() + cfg.getTime() * 1000;
		mapRoleBuild.setEndTime(endTime);
		mapRoleBuild.setOperationType(operationType);

		buildMgrImpl.putInOperationBild(mapRoleBuild);

		syncExecute(mapRoleBuild.getRid(), mapRoleBuild);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 数据同步
	 *
	 * @param mapBuild
	 */
	@Override
	public void syncExecute(Integer rid, MapBuildDTO mapBuild) {
		// 2DB
		buildMgrImpl.saveOrUpdateBuild2DB(mapBuild);

		pushData(rid, mapBuild);
	}

	/**
	 * onTaskProcessed 每秒回调一次，累计回调次数
	 * 定时（> 50）清理已处理的任务
	 */
	private int onTaskProcessedCallCount = 0;

	/**
	 * 在任务执行过程中，任务相关或其它的，需要做随着时间而变化的状态更新等等的过程变量更新
	 * 在Service层实现
	 */

	@Async(AsyncThreadPoolConfig.ASYNC_TASK_EXECUTOR_NAME)
	@Override
	public void onTaskProcessed() {
		checkBuildingState();

		onTaskProcessedCallCount++;

		if (onTaskProcessedCallCount >= 50) {
			buildMgrImpl.checkPendingMapEmpty();
			onTaskProcessedCallCount = 0;
		}
	}

	private void checkBuildingState() {
		List<Integer> giveUpPosIds = checkGiveUp();
		giveUpPosIds.forEach(posId -> {
			armyLogic.updateGiveUpPosArmy(posId);
		});

		List<Integer> destroyPosIds = checkInOperationBilds();
		destroyPosIds.forEach(posId -> {
			armyLogic.updateStopInPosArmy(posId);
		});
	}


	/**
	 * 检测正在放弃的土地是否到期了
	 *
	 * @return
	 */
	private List<Integer> checkGiveUp() {
		List<Integer> ret = new ArrayList<>();

		List<MapBuildDTO> beforeList = buildMgrImpl.getCurBeforeGiveUpBuilds();
		if (beforeList != null && beforeList.size() > 0) {
			List<MapBuildDTO> builds = new ArrayList<>();

			beforeList.forEach(mapBuild -> {
				buildMgrImpl.delGiveUpBuild(mapBuild);
				builds.add(mapBuild);
				ret.add(CityPositionUtils.position2Number(mapBuild.getX(), mapBuild.getY()));
			});

			builds.forEach(build -> {
				removeFromRole(build);
			});

		}
		return ret;
	}

	/**
	 * 检测正在执行操作的建筑是否完成
	 *
	 * @return
	 */
	private List<Integer> checkInOperationBilds() {
		List<Integer> ret = new ArrayList<>();

		List<MapBuildDTO> beforeList = buildMgrImpl.getCurBeforeInOperationBilds();
		if (beforeList != null && beforeList.size() > 0) {
			List<MapBuildDTO> builds = new ArrayList<>();

			beforeList.forEach(mapBuild -> {
				buildMgrImpl.delInOperationBilds(mapBuild);
				builds.add(mapBuild);
				ret.add(CityPositionUtils.position2Number(mapBuild.getX(), mapBuild.getY()));
			});

			// 执行操作
			builds.forEach(build -> {
				operationBild(build);
			});
		}

		return ret;
	}

	private void operationBild(MapBuildDTO build){
		Integer rid = build.getRid();
		switch (build.getOperationType()){
			case BUILD_NEW, BUILD_UP -> buildMgrImpl.clearOperationState(build);
			case BUILD_DEL -> buildMgrImpl.convertToRes(build);
			default -> {
				return;
			}
		}

		syncExecute(rid, build);
	}

	/**
	 * 移除建筑
	 *
	 * @param build
	 */
	private synchronized void removeFromRole(MapBuildDTO build) {
		// 移除旧主
		buildMgrImpl.delRoleBuilds(build);

		//移除放弃事件
		buildMgrImpl.delGiveUpBuild(build);

		//移除拆除事件
		buildMgrImpl.delInOperationBilds(build);

		Integer rid = build.getRid();
		buildMgrImpl.reset(build);

		syncExecute(rid, build);
	}

}
