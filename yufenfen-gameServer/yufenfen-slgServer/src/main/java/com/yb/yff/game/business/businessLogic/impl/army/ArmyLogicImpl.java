package com.yb.yff.game.business.businessLogic.impl.army;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.*;
import com.yb.yff.game.business.businessLogic.IArmyLogic;
import com.yb.yff.game.business.businessLogic.INationMapLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.business.businessLogic.IWarReportLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.business.businessLogic.impl.map.MapPositionLogic;
import com.yb.yff.game.business.businessLogic.impl.war.ArmyWarLogic;
import com.yb.yff.game.data.constant.myEnum.ArmyCmd;
import com.yb.yff.game.data.constant.myEnum.ArmyState;
import com.yb.yff.game.data.constant.myEnum.FacilityType;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.ConscriptCheckResultDTO;
import com.yb.yff.game.data.dto.ConscriptTaskTCTP;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.*;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.FacilityDTO;
import com.yb.yff.game.data.dto.city.NationalMap;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.dto.role.RoleResourceDTO;
import com.yb.yff.game.data.dto.role.RoleResourceData;
import com.yb.yff.game.jsondb.data.dto.BasicConscript;
import com.yb.yff.game.service.DelayedTask.ITaskExecutionProcessListener;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.utils.PositionUtils;
import com.yb.yff.game.utils.TaskUitls;
import com.yb.yff.sb.config.AsyncThreadPoolConfig;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 地图军队管理
 */
@Service
@Slf4j
public class ArmyLogicImpl extends BusinessDataSyncImpl<ArmyDTO> implements IArmyLogic, ITaskExecutionProcessListener {
	final JsonConfigMgr jsonConfigMgr;

	final IRoleLogic roleLogic;

	final ArmyMgrImpl armyMgr;

	final GeneralMgrImpl generalMgr;

	final BuildMgrImpl buildMgr;

	final CityMgrImpl cityMgr;


	final CityFacilityMgrImpl cityFacilityMgr;


	final RoleDataMgrImpl roleDataManager;


	final MapPositionLogic mapPositionLogic;


	final INationMapLogic nationMapLogic;


	final ArmyWarLogic armyWarLogic;

	final WarReportMgrImpl warReportMgr;

	final IWarReportLogic warReportLogic;

	final IPushService pushService;

	final BusinessDataSyncImpl<RoleResourceData> roleDataPusher;
	final BusinessDataSyncImpl<WarReportDTO> warReportDataPusher;

	private ArmyCmdLogic armyCmdLogic;

	@Autowired
	public ArmyLogicImpl(@Lazy ArmyWarLogic armyWarLogic, @Lazy INationMapLogic nationMapLogic,
	                     JsonConfigMgr jsonConfigMgr, ArmyMgrImpl armyMgr,
	                     IWarReportLogic warReportLogic, WarReportMgrImpl warReportMgr, IRoleLogic roleLogic,
	                     BusinessDataSyncImpl<WarReportDTO> warReportDataPusher,  RoleDataMgrImpl roleDataManager,
	                     BusinessDataSyncImpl<RoleResourceData> roleDataPusher, GeneralMgrImpl generalMgr,
	                     CityMgrImpl cityMgr, CityFacilityMgrImpl cityFacilityMgr, IPushService pushService,
	                     BuildMgrImpl buildMgr, MapPositionLogic mapPositionLogic) {
		this.warReportLogic = warReportLogic;
		this.roleDataPusher = roleDataPusher;
		this.warReportDataPusher = warReportDataPusher;
		this.armyWarLogic = armyWarLogic;
		this.roleLogic = roleLogic;
		this.warReportMgr = warReportMgr;
		this.pushService = pushService;
		this.jsonConfigMgr = jsonConfigMgr;
		this.roleDataManager = roleDataManager;
		this.cityMgr = cityMgr;
		this.cityFacilityMgr = cityFacilityMgr;
		this.buildMgr = buildMgr;
		this.mapPositionLogic = mapPositionLogic;
		this.nationMapLogic = nationMapLogic;
		this.armyMgr = armyMgr;
		this.generalMgr = generalMgr;
	}

	@PostConstruct
	protected void Init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_army, pushService);

		armyCmdLogic = new ArmyCmdLogic(jsonConfigMgr, roleDataManager, roleDataPusher, warReportDataPusher,
				buildMgr, cityMgr, this, mapPositionLogic, nationMapLogic, armyMgr, armyWarLogic);
	}


	/**
	 * 获取城市军队
	 *
	 * @param cityId
	 * @return
	 */
	@Override
	public List<WarArmyDTO> getCityArmy(Integer cityId) {
		return armyMgr.getCityArmy(cityId);
	}

	/**
	 * 获取指定军队
	 *
	 * @param cityId
	 * @param order
	 * @return
	 */
	@Override
	public WarArmyDTO getOrderArmy(Integer rid, Integer cityId, Integer order) {
		return armyMgr.getOrCreateArmy(rid, cityId, order);
	}

	/**
	 * 配置将领
	 *
	 * @param disposeDTO
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<WarArmyDTO> configureGenerals(Integer rid, DisposeDTO disposeDTO) {
		LogicTaskResultDTO<WarArmyDTO> result = new LogicTaskResultDTO<>();

		GeneralDTO general = generalMgr.getRoleGeneral(disposeDTO.getGeneralId());
		if (general == null) {
			result.setCode(NetResponseCodeConstants.GeneralNotFound);
			return result;
		}

		if (general.getRid() != rid) {
			result.setCode(NetResponseCodeConstants.GeneralNotMe);
			return result;
		}

		CityDTO city = cityMgr.getCity(disposeDTO.getCityId());
		if (city == null) {
			result.setCode(NetResponseCodeConstants.CityNotExist);
			return result;
		}

		// 根据设施等级，检查军队数量, 校场级数决定军队个数
		FacilityDTO jiaoChang = cityFacilityMgr.getFacility(rid, disposeDTO.getCityId(), FacilityType.JiaoChang.getValue());
		if (jiaoChang.getLevel() < disposeDTO.getOrder()) {
			result.setCode(NetResponseCodeConstants.ArmyNotEnough);
			return result;
		}

		// 配置将领
		Integer position = disposeDTO.getPosition();
		ResponseCode configure;
		if (position == -1) {
			configure = exitArmy(rid, general, disposeDTO);
		} else {
			configure = JoinArmy(rid, general, disposeDTO);
		}
		if (configure != NetResponseCodeConstants.SUCCESS) {
			result.setCode(configure);
			return result;
		}

		WarArmyDTO army = armyMgr.getOrCreateArmy(rid, disposeDTO.getCityId(), disposeDTO.getOrder());
		result.setResult(army);

		result.setCode(NetResponseCodeConstants.SUCCESS);
		return result;
	}

	/**
	 * 配置士兵
	 *
	 * @param rid
	 * @param conscriptDTO
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<TimeConsumingTask>> configureSoldiers(
			GameMessageEnhancedReqDTO reqDTO, Integer rid, ConscriptDTO conscriptDTO) {
		LogicTaskResultDTO<List<TimeConsumingTask>> result = new LogicTaskResultDTO<>();

		// 前置检测
		LogicTaskResultDTO<ConscriptCheckResultDTO> check = checkCanSoldiers(rid, conscriptDTO);
		if (check.getCode() != NetResponseCodeConstants.SUCCESS) {
			result.setCode(check.getCode());
			return result;
		}

		ConscriptCheckResultDTO resourceNeed = check.getResult();

		// 买单
		buyOrder(rid, resourceNeed.getNeedResourceTotal());

		// 开始执行征兵
		startConscript(conscriptDTO, resourceNeed.getNeedResourceList());

		// 封装延时任务完成时的返回数据
		ConscriptResDTO conscriptResDTO = new ConscriptResDTO();
		conscriptResDTO.setArmy(armyMgr.getArmy(conscriptDTO.getArmyId()).getArmy());
		conscriptResDTO.setRole_res(roleDataManager.getRoleResourceData(rid));
		conscriptResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		GameMessageEnhancedResDTO gameMessageEnhancedResDTO = new GameMessageEnhancedResDTO();
		BeanUtils.copyProperties(reqDTO, gameMessageEnhancedResDTO);
		gameMessageEnhancedResDTO.setMsg(conscriptResDTO);

		// 耗时任务- 征兵
		List<TimeConsumingTask> tctList = createConscriptTasks(rid, reqDTO, conscriptDTO, resourceNeed, gameMessageEnhancedResDTO);

		result.setResult(tctList);

		result.setCode(NetResponseCodeConstants.SUCCESS);
		return result;
	}

	/**
	 * 军队调遣
	 *
	 * @param rid
	 * @param assignDTO
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<WarArmyDTO> assignArmy(Integer rid, AssignDTO assignDTO) {
		LogicTaskResultDTO<WarArmyDTO> result = new LogicTaskResultDTO<>();

		WarArmyDTO army = armyMgr.getArmy(assignDTO.getArmyId());

		if (army == null) {
			result.setCode(NetResponseCodeConstants.ArmyNotFound);
			return result;
		}

		if (!rid.equals(army.getArmy().getRid())) {
			result.setCode(NetResponseCodeConstants.ArmyNotMe);
			return result;
		}

		// 处理新指令
		ResponseCode responseCode = armyCmdLogic.processingCommands(army, assignDTO);
		if (responseCode != NetResponseCodeConstants.SUCCESS) {
			result.setCode(responseCode);
			return result;
		}

		result.setResult(army);
		result.setCode(NetResponseCodeConstants.SUCCESS);
		return result;
	}

	@Override
	public synchronized void armyBack(WarArmyDTO army) {
		armyMgr.clearConscript(army.getArmy());

		army.getArmy().setState(ArmyState.ArmyRunning.getValue());
		army.getArmy().setCmd(ArmyCmd.ArmyCmdBack.getValue());

		armyCmdLogic.pushAction(army);
	}


	/**
	 * 获取地图单元格的NPC军团
	 *
	 * @param posX, posY
	 */
	@Override
	public synchronized List<WarArmyDTO> getNpcArmyListByPos(Integer posX, Integer posY) {
		List<WarArmyDTO> stopInPosArmyList = armyMgr.getStopInPosNPCArmyList(posX, posY);
		if (stopInPosArmyList.size() > 0) {
			return stopInPosArmyList;
		}


		NationalMap cellData = buildMgr.getCellConfigData(posX, posY);
		Integer level = cellData.getLevel();
		Integer posId = CityPositionUtils.position2Number(posX, posY);
		List<WarArmyDTO> npcArmyConfigList = armyMgr.getNpcArmyList(level);

		List<WarArmyDTO> posNpcArmyList = new ArrayList<>();

		// 根据配置 拷贝
		npcArmyConfigList.forEach(npcArmy -> {
			// BeanUtils.copyProperties为浅拷贝，这里要深拷贝, 用JSONObject.parseObject深拷贝
			// WarArmyDTO posNpcArmy = new WarArmyDTO();
			// BeanUtils.copyProperties(npcArmy, posNpcArmy);
			String jsonStr = JSONObject.toJSONString(npcArmy);
			WarArmyDTO posNpcArmy = JSONObject.parseObject(jsonStr, WarArmyDTO.class);
			posNpcArmyList.add(posNpcArmy);
		});


		// 记录NPC军团
		armyMgr.putStopInPosNPCArmys(posId, posNpcArmyList);

		return posNpcArmyList;
	}

	/**
	 * 删除NPC军队
	 *
	 * @param toX, toY
	 */
	@Override
	public void removeNPCArmy(Integer toX, Integer toY) {
		Integer posId = CityPositionUtils.position2Number(toX, toY);
		armyMgr.deleteStopInPosNPCArmys(posId);
	}

	/**
	 * 放弃驻守
	 *
	 * @param posId
	 */
	public void updateGiveUpPosArmy(Integer posId) {
		//在该位置驻守、调动的都需要返回
		List<WarArmyDTO> armys = armyMgr.getStopInPosArmyMap(posId);

		if (armys != null && armys.size() > 0) {
			armys.forEach(army -> {
				armyBack(army);
			});

			armyMgr.delFromStopInPosArmyMap(posId);
		}
	}

	/**
	 * 只有调动到该位置的军队需要返回
	 *
	 * @param posId
	 */
	@Override
	public void updateStopInPosArmy(Integer posId) {
		//
		List<WarArmyDTO> targets = new ArrayList<>();

		synchronized (this) {
			List<WarArmyDTO> armys = armyMgr.getStopInPosArmyMap(posId);
			if (armys != null && armys.size() > 0) {
				armys.forEach(army -> {
					ArmyDTO armyDTO = army.getArmy();
					if (PositionUtils.equalsPosition(armyDTO.getFrom_x(), armyDTO.getFrom_y(),
							armyDTO.getTo_x(), armyDTO.getTo_y())) {
						targets.add(army);
						armys.remove(army);
					}
				});
			}
		}

		targets.forEach(army -> armyBack(army));
	}

	/**
	 * 创建征兵任务列表
	 * 每个将领的征兵数量基本上是不一致的，它们的时长也不一致，所以需要创建多个任务
	 * 耗时任务的Key：
	 * 最大耗时的任务：请求字串
	 * 其它任务：请求字串 + "_" + index
	 *
	 * @return
	 */
	List<TimeConsumingTask> createConscriptTasks(Integer rid,
	                                             GameMessageEnhancedReqDTO reqDTO,
	                                             ConscriptDTO conscriptDTO,
	                                             ConscriptCheckResultDTO resourceNeed,
	                                             GameMessageEnhancedResDTO gameMessageEnhancedResDTO) {

		String taskKey = TaskUitls.createTaskKey(reqDTO);

		List<BasicConscript> needResourceList = resourceNeed.getNeedResourceList();

		List<TimeConsumingTask> tasks = new ArrayList<>();
		boolean findLastTime = false;
		for (int index = 0; index < needResourceList.size(); index++) {
			int time = needResourceList.get(index).getCost_time();

			String taskId = taskKey + "_" + index;

			if (!findLastTime && time == resourceNeed.getNeedResourceTotal().getCost_time()) {
				taskId = taskKey;
				findLastTime = true;
			}
			Long currentTime = System.currentTimeMillis() / 1000;
			int cost_time = time + currentTime.intValue();

			TimeConsumingTask task = createTimeConsumingTask(rid,
					conscriptDTO.getArmyId(),
					cost_time,
					time,
					conscriptDTO.getCnts().get(index),
					index,
					taskId,
					gameMessageEnhancedResDTO);

			tasks.add(task);
		}

		return tasks;
	}


	private TimeConsumingTask createTimeConsumingTask(Integer rid, Integer aid, Integer times, Integer delayTime,
	                                                  Integer soldiers, Integer postion, String taskKey,
	                                                  GameMessageEnhancedResDTO gameMessageEnhancedResDTO) {

		ConscriptTaskTCTP conscriptTaskTCTP = new ConscriptTaskTCTP();
		conscriptTaskTCTP.setRid(rid);
		conscriptTaskTCTP.setArmyId(aid);
		conscriptTaskTCTP.setPositon(postion);
		conscriptTaskTCTP.setTimes(delayTime);
		conscriptTaskTCTP.setSoldiers(soldiers);

		Duration delay = Duration.ofSeconds(delayTime);

		// 创建 [征兵] 耗时任务
		log.info("================== 开始执行 耗时任务 【军团{} 将领{} 耗时 {}秒 征兵{}】",
				conscriptTaskTCTP.getArmyId(),
				conscriptTaskTCTP.getPositon(),
				conscriptTaskTCTP.getTimes(),
				conscriptTaskTCTP.getSoldiers());

		TimeConsumingTask task = new TimeConsumingTask(taskKey, delay, "",
				gameMessageEnhancedResDTO, null, conscriptTaskTCTP, (param) -> {
			// 完成征兵
			ConscriptTaskTCTP conscriptParam = (ConscriptTaskTCTP) param;

			finishConscript(conscriptParam);

			log.info("================== 耗时任务 【军团{} 将领{} 耗时 {}秒 征兵{}】 执行完成！",
					conscriptParam.getArmyId(),
					conscriptParam.getPositon(),
					conscriptParam.getTimes(),
					conscriptParam.getSoldiers());
		});

		return task;
	}

	/**
	 * 征兵 买单
	 *
	 * @param rid
	 * @param resourceNeed
	 */
	private void buyOrder(Integer rid, BasicConscript resourceNeed) {
		// 扣除资源消耗
		RoleResourceDTO cost = new RoleResourceDTO();
		cost.setGrain(resourceNeed.getCost_grain());
		cost.setIron(resourceNeed.getCost_iron());
		cost.setStone(resourceNeed.getCost_stone());
		cost.setWood(resourceNeed.getCost_wood());
		cost.setGold(resourceNeed.getCost_gold());

		// DB
		roleLogic.updateRoleResource(rid, cost, false);
	}

	/**
	 * 开始征兵
	 *
	 * @param conscriptDTO
	 * @param needResourceList
	 */
	private void startConscript(ConscriptDTO conscriptDTO, List<BasicConscript> needResourceList) {
		WarArmyDTO warArmy = armyMgr.getArmy(conscriptDTO.getArmyId());

		ArmyDTO army = warArmy.getArmy();

		// 设置 征兵指令
		army.setCmd(ArmyCmd.ArmyCmdConscript.getValue());

		army.setCon_cnts(conscriptDTO.getCnts());

		List<Integer> con_times = new ArrayList<>();
		needResourceList.forEach(time -> {
			Long currentTime = System.currentTimeMillis() / 1000;
			int cost_time = time.getCost_time() + currentTime.intValue();
			con_times.add(cost_time + 2); // 推送给客户端的时间，稍晚于 延时任务的执行时间
		});
		army.setCon_times(con_times);

		// 更新DB
		ArmyDTO updateArmy = new ArmyDTO();
		updateArmy.setId(army.getId());
		updateArmy.setCmd(army.getCmd());
		updateArmy.setCon_cnts(army.getCon_cnts());
		updateArmy.setCon_times(army.getCon_times());
		armyMgr.updateArmy2DB(updateArmy);
	}

	/**
	 * 完成征兵
	 *
	 * @param conscriptParam
	 */
	private synchronized void finishConscript(ConscriptTaskTCTP conscriptParam) {
		ArmyDTO army = armyMgr.getArmy(conscriptParam.getArmyId()).getArmy();
		army.getCon_cnts().set(conscriptParam.getPositon(), 0);

		army.getCon_times().set(conscriptParam.getPositon(), 0);

		AtomicInteger otherCnt = new AtomicInteger();
		army.getCon_cnts().forEach(cnt -> otherCnt.addAndGet(cnt));

		// 设置 空闲指令
		if (army.getCmd() == ArmyCmd.ArmyCmdConscript.getValue() && otherCnt.get() == 0) {
			army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
		}

		// 更新士兵数量
		List<Integer> newSoldiers = new ArrayList<>();
		newSoldiers.addAll(army.getSoldiers());
		newSoldiers.set(conscriptParam.getPositon(), newSoldiers.get(conscriptParam.getPositon()) + conscriptParam.getSoldiers());
		army.setSoldiers(newSoldiers);

		// 更新DB
		ArmyDTO updateArmy = new ArmyDTO();
		updateArmy.setId(army.getId());
		updateArmy.setCmd(army.getCmd());
		updateArmy.setCon_cnts(army.getCon_cnts());
		updateArmy.setCon_times(army.getCon_times());
		updateArmy.setSoldiers(army.getSoldiers());
		armyMgr.updateArmy2DB(updateArmy);
	}

	/**
	 * 是否满足征兵条件
	 *
	 * @param conscriptDTO
	 * @return
	 */
	private LogicTaskResultDTO<ConscriptCheckResultDTO> checkCanSoldiers(Integer rid, ConscriptDTO conscriptDTO) {
		LogicTaskResultDTO<ConscriptCheckResultDTO> result = new LogicTaskResultDTO<>();

		ArmyDTO army = armyMgr.getArmy(conscriptDTO.getArmyId()).getArmy();
		if (army == null) {
			result.setCode(NetResponseCodeConstants.ArmyNotFound);
			return result;
		}

		// 检测 将领是否在军队中执行任务
		ResponseCode busy = checkGeneralIsBusy(army, conscriptDTO);
		if (busy != NetResponseCodeConstants.SUCCESS) {
			result.setCode(busy);
			return result;
		}

		// 检测 募兵所等级
		FacilityDTO facility = cityFacilityMgr.getFacility(rid, army.getCityId(), FacilityType.MuBingSuo.getValue());
		if (facility.getLevel() < 0) {
			result.setCode(NetResponseCodeConstants.BuildMBSNotFound);
			return result;
		}

		//检测 征兵数量
		ResponseCode limitResut = checkSoldiersLimit(army, conscriptDTO);
		if (limitResut != NetResponseCodeConstants.SUCCESS) {
			result.setCode(limitResut);
			return result;
		}

		// 检测 资源
		LogicTaskResultDTO<ConscriptCheckResultDTO> ressourceResult = checkResource(rid, conscriptDTO.getCnts());
		if (ressourceResult.getCode() != NetResponseCodeConstants.SUCCESS) {
			result.setCode(ressourceResult.getCode());
			return result;
		}

		result.setResult(ressourceResult.getResult());
		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}

	/**
	 * 加入军队
	 *
	 * @param rid
	 * @param general
	 * @param disposeDTO
	 * @return
	 */
	private synchronized ResponseCode JoinArmy(Integer rid, GeneralDTO general, DisposeDTO disposeDTO) {

		ArmyDTO army = armyMgr.getOrCreateArmy(rid, disposeDTO.getCityId(), disposeDTO.getOrder()).getArmy();

		// 将领是否在军队中执行任务
		ResponseCode checkState = checkGeneralStateInArmy(army, disposeDTO.getPosition());
		if (checkState != NetResponseCodeConstants.SUCCESS) {
			return checkState;
		}

		// 重复任职
		if (generalIsRepeat(general)) {
			return NetResponseCodeConstants.GeneralRepeat;
		}

		// 职位条件
		if (!checkArmyPosition(army, disposeDTO.getPosition())) {
			return NetResponseCodeConstants.TongShuaiNotEnough;
		}

		// TODO 判断cost，后续该为主将统帅力，暂时先用cost代替
		if (!checkCommanderAbility(disposeDTO.getPosition(), general, army)) {
			return NetResponseCodeConstants.CostNotEnough;
		}

		// 原坑位的先退出
		GeneralDTO oldGeneral = generalMgr.getRoleGeneral(army.getGenerals().get(disposeDTO.getPosition()));
		if (oldGeneral != null) {
			ResponseCode exit = exitArmy(disposeDTO.getPosition(), oldGeneral, disposeDTO);
			if (exit != NetResponseCodeConstants.SUCCESS) {
				return exit;
			}
		}

		// 上任
		List<Integer> newGeneralList = new ArrayList<>();
		newGeneralList.addAll(army.getGenerals());
		newGeneralList.set(disposeDTO.getPosition(), general.getId());
		army.setGenerals(newGeneralList);
		army.getSoldiers().set(disposeDTO.getPosition(), 0);

		general.setOrder(army.getOrder());
		general.setCityId(army.getCityId());

		// 数据库更新
		ArmyDTO updateArmy = new ArmyDTO();
		updateArmy.setId(army.getId());
		updateArmy.setGenerals(army.getGenerals());
		updateArmy.setSoldiers(army.getSoldiers());
		armyMgr.updateArmy2DB(updateArmy);

		GeneralDTO updateGeneral = new GeneralDTO();
		updateGeneral.setId(general.getId());
		updateGeneral.setOrder(general.getOrder());
		updateGeneral.setCityId(general.getCityId());
		generalMgr.updateGeneral2DB(updateGeneral);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 退出军队
	 *
	 * @param rid
	 * @param general
	 * @param disposeDTO
	 * @return
	 */
	private ResponseCode exitArmy(Integer rid, GeneralDTO general, DisposeDTO disposeDTO) {
		if (general == null) {
			return NetResponseCodeConstants.GeneralNotFound;
		}

		ArmyDTO army = armyMgr.getOrCreateArmy(rid, disposeDTO.getCityId(), disposeDTO.getOrder()).getArmy();

		// 卸任时，客户端发送的参数值为 -1，需要找到将领正确的坑位
		Integer position = disposeDTO.getPosition();
		if (position == -1) {
			for (int index = 0; index < army.getGenerals().size(); index++) {
				Integer gid = army.getGenerals().get(index);
				if (gid.equals(general.getId())) {
					position = index;
					break;
				}
			}
		}
		// 将领不在军队中
		if (position == -1) {
			return NetResponseCodeConstants.InvalidParam;
		}

		// 将领是否在军队中执行任务
		ResponseCode checkState = checkGeneralStateInArmy(army, position);
		if (checkState != NetResponseCodeConstants.SUCCESS) {
			return checkState;
		}

		// TODO 兵种回收
		Integer soldiers = army.getSoldiers().get(position);

		// 卸任
		army.getGenerals().set(position, 0);
		army.getSoldiers().set(position, 0);

		// 更新DB
		ArmyDTO updateArmy = new ArmyDTO();
		updateArmy.setId(army.getId());
		updateArmy.setGenerals(army.getGenerals());
		updateArmy.setSoldiers(army.getGenerals());
		armyMgr.updateArmy2DB(army);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 将领是否在军队中执行任务
	 *
	 * @param army
	 * @param armyPosition
	 * @return
	 */
	private ResponseCode checkGeneralStateInArmy(ArmyDTO army, Integer armyPosition) {
		if (armyPosition >= 3 || armyPosition < 0) {
			return NetResponseCodeConstants.InvalidParam;
		}

		if (army.getCmd() == null) {
			army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
		}

		if (army.getCmd() != ArmyCmd.ArmyCmdIdle.getValue()) {
			if (army.getCmd() == ArmyCmd.ArmyCmdConscript.getValue()) {
				if (army.getCon_times().get(armyPosition) == 0) {
					return NetResponseCodeConstants.GeneralBusy;
				} else {
					return NetResponseCodeConstants.SUCCESS;
				}
			}

			return NetResponseCodeConstants.ArmyBusy;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 将领是否重复任职
	 *
	 * @return
	 */
	private synchronized boolean generalIsRepeat(GeneralDTO general) {
		Integer rid = general.getRid();
		Integer gid = general.getId();

		List<WarArmyDTO> armys = armyMgr.getRoleArmy(rid);

		for (WarArmyDTO army : armys) {
			for (Integer gidInArmy : army.getArmy().getGenerals()) {
				if (gidInArmy == gid) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 军队职位是否满足条件
	 * [0-主将, 1-副将，2-前锋]
	 *
	 * @param army
	 * @param armyPosition
	 * @return
	 */
	private boolean checkArmyPosition(ArmyDTO army, Integer armyPosition) {
		// 主将与副将 不需要其它前置条件
		if (armyPosition < 2) {
			return true;
		}

		// 统帅厅级别 决定对应的 军团番号 是否可配置前锋
		FacilityDTO tongShuaiTing = cityFacilityMgr.getFacility(army.getRid(), army.getCityId(), FacilityType.TongShuaiTing.getValue());
		if (tongShuaiTing.getLevel() >= army.getOrder()) {
			return true;
		}

		return false;
	}

	/**
	 * 统帅能力是否达到条件
	 *
	 * @return
	 */
	private synchronized boolean checkCommanderAbility(Integer position, GeneralDTO general, ArmyDTO army) {
//		Integer costs = general.getCost();
//
//		for (Integer gid : army.getGenerals()) {
//			GeneralDTO orderGeneral = generalMgr.getRoleGeneral(gid);
//			if (gid == position || orderGeneral == null) {
//				continue;
//			}
//			costs += orderGeneral.getCost();
//		}


		return true;
	}

	/**
	 * 检查将领是否在军队中执行任务
	 *
	 * @return
	 */
	private ResponseCode checkGeneralIsBusy(ArmyDTO army, ConscriptDTO conscriptDTO) {
		List<Integer> cnts = conscriptDTO.getCnts();
		List<Integer> gids = army.getGenerals();

		for (Integer index = 0; index < cnts.size(); index++) {
			if (cnts.get(index) > 0) {
				GeneralDTO general = generalMgr.getRoleGeneral(gids.get(index));
				if (general == null) {
					return NetResponseCodeConstants.InvalidParam;
				}

				ResponseCode checkState = checkGeneralStateInArmy(army, index);
				if (checkState != NetResponseCodeConstants.SUCCESS) {
					return checkState;
				}
			}
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 检查征兵上限
	 *
	 * @return
	 */
	private ResponseCode checkSoldiersLimit(ArmyDTO army, ConscriptDTO conscriptDTO) {
		List<Integer> cnts = conscriptDTO.getCnts();
		List<Integer> gids = army.getGenerals();

		for (Integer index = 0; index < cnts.size(); index++) {
			GeneralDTO general = generalMgr.getRoleGeneral(gids.get(index));
			if (general == null) {
				continue;
			}

			Integer maxSoldierNum = generalMgr.getGCSoldierNum(general.getLevel());
			Integer existingNum = army.getSoldiers().get(index);
			Integer addNum = cnts.get(index);

			if (existingNum + addNum > 10 * maxSoldierNum) {
				return NetResponseCodeConstants.OutArmyLimit;
			}
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 检查资源是否满足
	 *
	 * @param rid
	 */
	private LogicTaskResultDTO<ConscriptCheckResultDTO> checkResource(Integer rid, List<Integer> conscriptList) {
		LogicTaskResultDTO<ConscriptCheckResultDTO> result = new LogicTaskResultDTO<>();

		ConscriptCheckResultDTO checkResult = new ConscriptCheckResultDTO();

		RoleResourceData roleResource = roleDataManager.getRoleResourceData(rid);

		List<BasicConscript> costList = new ArrayList<>();

		BasicConscript costOne = roleDataManager.getConscriptConfig();

		BasicConscript costTotal = new BasicConscript(0);

		conscriptList.forEach(num -> {
			BasicConscript cost = new BasicConscript();
			cost.setCost_gold(costOne.getCost_gold() * num);
			cost.setCost_stone(costOne.getCost_stone() * num);
			cost.setCost_grain(costOne.getCost_grain() * num);
			cost.setCost_iron(costOne.getCost_iron() * num);
			cost.setCost_wood(costOne.getCost_wood() * num);
			cost.setCost_time(costOne.getCost_time() * num);
			costList.add(cost);

			costTotal.setCost_gold(costTotal.getCost_gold() + cost.getCost_gold());
			costTotal.setCost_stone(costTotal.getCost_stone() + cost.getCost_stone());
			costTotal.setCost_grain(costTotal.getCost_grain() + cost.getCost_grain());
			costTotal.setCost_iron(costTotal.getCost_iron() + cost.getCost_iron());
			costTotal.setCost_wood(costTotal.getCost_wood() + cost.getCost_wood());
			// 总时间为，个体最大时间
			Integer totalTime = costTotal.getCost_time() < cost.getCost_time() ? cost.getCost_time() : costTotal.getCost_time();
			costTotal.setCost_time(totalTime);
		});


		if (roleResource.getGold() < costTotal.getCost_gold()
				|| roleResource.getStone() < costTotal.getCost_stone()
				|| roleResource.getGrain() < costTotal.getCost_grain()
				|| roleResource.getIron() < costTotal.getCost_iron()
				|| roleResource.getWood() < costTotal.getCost_wood()) {
			result.setCode(NetResponseCodeConstants.ResNotEnough);
			return result;
		}

		checkResult.setNeedResourceList(costList);
		checkResult.setNeedResourceTotal(costTotal);

		result.setResult(checkResult);
		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}


	/**
	 * 记录 次数
	 */
	private int callBackTaskProcessNum = 0;

	/**
	 * 每秒回调一次
	 * 在任务执行过程中，任务相关或其它的，需要做随着时间而变化的状态更新等等的过程变量更新
	 * 在Service层实现
	 */

	@Async(AsyncThreadPoolConfig.ASYNC_TASK_EXECUTOR_NAME)
	@Override
	public void onTaskProcessed() {
		updatePassBy();

		// 每三秒处理一次
		if (callBackTaskProcessNum >= 3) {
			callBackTaskProcessNum = 0;

			check();
		} else {
			callBackTaskProcessNum++;
		}
	}

	/**
	 * 更新 army 移动路径
	 */
	public synchronized void updatePassBy() {

		Map<Integer, Map<Integer, WarArmyDTO>> temp = new HashMap<>();

		armyMgr.getOutArmyMap().entrySet().forEach(entry -> {
			entry.getValue().forEach(army -> {
				if (army.getArmy().getState().equals(ArmyState.ArmyRunning.getValue())) {
					PositionDTO currentPosition = armyMgr.countCurrentPosition(army.getArmy());
					Integer posId = CityPositionUtils.position2Number(currentPosition);

					if (!temp.containsKey(posId)) {
						temp.put(posId, new HashMap<>());
					}
					temp.get(posId).put(army.getArmy().getId(), army);
					checkSyncCell(army);
				}

			});
		});

		armyMgr.getStopInPosArmyMap().entrySet().forEach(entry -> {
			entry.getValue().forEach(army -> {
				if (!temp.containsKey(entry.getKey())) {
					temp.put(entry.getKey(), new HashMap<>());
				}
				temp.get(entry.getKey()).put(army.getArmy().getId(), army);
			});
		});

		armyMgr.setPassByPosArmyMap(temp);
	}

	/**
	 * 检测 Army 是否到达目的地
	 */
	public synchronized void check() {

		Long now = System.currentTimeMillis() / 1000;

		Map<Integer, List<WarArmyDTO>> endTimeArmyAll = armyMgr.getEndTimeArmyAll();
		if (endTimeArmyAll.size() == 0) {
			return;
		}

		endTimeArmyAll.forEach((endTime, warArmy) -> {
			if (now.intValue() > endTime) {

				if (warArmy != null && warArmy.size() > 0) {
					warArmy.forEach(army -> {
						armyCmdLogic.exeArrive(army);
					});
				}
				armyMgr.removEndTimeArmy(endTime); // 直接移除元素
			}
		});
	}


	/**
	 * 更新 军团 单元格位置
	 *
	 * @param army
	 */
	@Override
	public void checkSyncCell(WarArmyDTO army) {
		PositionDTO currentPosition = armyMgr.countCurrentPosition(army.getArmy());
		if (currentPosition.getX() != army.getCellX() || currentPosition.getY() != army.getCellY()) {
			this.syncExecute(army.getArmy().getRid(), army.getArmy());

			// 刷新坐标
			army.setCellX(currentPosition.getX());
			army.setCellY(currentPosition.getY());
		}
	}

	/**
	 * 同步数据
	 *
	 * @param army
	 */
	public void syncExecute(Integer rid, ArmyDTO army) {
		armyMgr.updateArmy2DB(army);

		pushData(rid, army);
	}
}
