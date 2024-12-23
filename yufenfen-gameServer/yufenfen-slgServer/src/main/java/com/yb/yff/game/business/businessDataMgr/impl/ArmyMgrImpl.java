package com.yb.yff.game.business.businessDataMgr.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.constant.myEnum.ArmyCmd;
import com.yb.yff.game.data.constant.myEnum.FacilityAdditionType;
import com.yb.yff.game.data.dto.army.ArmyDTO;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.entity.ArmyEntity;
import com.yb.yff.game.jsondb.data.dto.general.GeneralList;
import com.yb.yff.game.jsondb.data.dto.general.General_armsArms;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_armyArmys;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_armyArmysArmy;
import com.yb.yff.game.service.IArmyService;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.utils.PositionUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
 * @Description: 军队管理
 */
@Component
@Slf4j
public class ArmyMgrImpl implements IJsonDataHandler {
	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	IArmyService armyService;

	@Autowired
	CityMgrImpl cityMgr;

	@Autowired
	GeneralMgrImpl generalMgr;

	@Autowired
	CityFacilityMgrImpl cityFacilityMgr;

	// 缓存数据库的军队 key:armyId
	Map<Integer, WarArmyDTO> dbArmyMap = new ConcurrentHashMap<>();

	// 城市中的军队， key:cityId
	Map<Integer, List<WarArmyDTO>> cityArmyMap = new ConcurrentHashMap<>();

	// 角色的军队 key: <rid, <order, ArmyDTO>>
	Map<Integer, Map<Integer, WarArmyDTO>> roleOrderArmyMap = new ConcurrentHashMap<>();

	// 城外的军队 key:armyId
	Map<Integer, List<WarArmyDTO>> outArmyMap = new ConcurrentHashMap<>();

	//  key: 到达时间戳，秒
	Map<Integer, List<WarArmyDTO>> endTimeArmyMap = new ConcurrentHashMap<>();

	// 玩家停留位置的军队 key:posId
	Map<Integer, List<WarArmyDTO>> stopInPosArmyMap = new ConcurrentHashMap<>();

	// 玩家停留位置的军队 key:posId
	Map<Integer, List<WarArmyDTO>> stopInPosNPCArmyMap = new ConcurrentHashMap<>();

	// 玩家路过位置的军队 key:posId,armyId
	Map<Integer, Map<Integer, WarArmyDTO>> passByPosArmyMap = new ConcurrentHashMap<>();

	/**
	 * NPC军队
	 * key:buildLevel
	 * 同一等级的地图单元格的军队一样
	 * 玩家攻击无归属的地图单元格时，拷贝一份同一等级NPC军团作为防守方。
	 */
	Map<Integer, List<WarArmyDTO>> npcArmyConfigMap = new HashMap<>();

	/******************************************/
	/*************** init start ***************/
	/******************************************/

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {
		if (dbArmyMap.size() > 0) {
			return;
		}

		// 查询数据库中是否有记录
		long count = armyService.count();

		if (count == 0) {
			return;
		}

		List<ArmyEntity> dbList = armyService.getBaseMapper().selectList(null);

		dbList.stream().forEach(armyEntity -> {
			WarArmyDTO armyDTO = armyEntity2ArmyDTO(armyEntity);

			addArmy2Cache(armyDTO);
		});

		initNPCArmy();
	}

	private void initNPCArmy() {
		List<Npc_armyArmys> npcArmyConfig = jsonConfigMgr.getNpcArmyConfig();

		AtomicInteger level = new AtomicInteger(1);

		npcArmyConfig.forEach(npcArmyArmys -> {

			List<WarArmyDTO> npcArmyDTOList = new ArrayList<>();

			Integer soldiers = npcArmyArmys.getSoldiers();
			npcArmyArmys.getArmy().forEach(armyData -> {

				WarArmyDTO npcArmyDTO = cretateNPCArmy(armyData, soldiers);

				npcArmyDTOList.add(npcArmyDTO);

			});

			npcArmyConfigMap.put(level.get(), npcArmyDTOList);

			level.getAndIncrement();
		});
	}

	private WarArmyDTO cretateNPCArmy(Npc_armyArmysArmy npcarmyConfigList, Integer soldiers) {
		List<GeneralDTO> npcGenerals = generalMgr.createNPCGenerals(npcarmyConfigList);

		List<Integer> gsId = List.of(0, 0, 0);

		List<Integer> scnt = List.of(soldiers, soldiers, soldiers);

		WarArmyDTO army = createArmy(0, 0, 0, gsId, scnt);

		army.setGeneralList(npcGenerals);

		return army;
	}

	private WarArmyDTO armyEntity2ArmyDTO(ArmyEntity armyEntity) {
		ArmyDTO armyDTO = new ArmyDTO();
		BeanUtils.copyProperties(armyEntity, armyDTO);

		armyDTO.setFromTo(armyEntity.getFromX(), armyEntity.getFromY(), armyEntity.getToX(), armyEntity.getToY());

		if (!StringUtil.isNullOrEmpty(armyEntity.getGenerals())) {
			List<Integer> generals = JSONArray.parseArray(armyEntity.getGenerals(), Integer.class);
			armyDTO.setGenerals(generals);
		}

		if (!StringUtil.isNullOrEmpty(armyEntity.getSoldiers())) {
			List<Integer> soldiers = JSONArray.parseArray(armyEntity.getSoldiers(), Integer.class);
			armyDTO.setSoldiers(soldiers);
		}

		if (!StringUtil.isNullOrEmpty(armyEntity.getConscriptCnts())) {
			List<Integer> conscript_cnts = JSONArray.parseArray(armyEntity.getConscriptCnts(), Integer.class);
			armyDTO.setCon_cnts(conscript_cnts);
		}

		if (!StringUtil.isNullOrEmpty(armyEntity.getConscriptTimes())) {
			List<Integer> conscript_times = JSONArray.parseArray(armyEntity.getConscriptTimes(), Integer.class);
			armyDTO.setCon_times(conscript_times);
		}

		return newWarArmy(armyDTO);
	}


	private ArmyEntity armyDTO2ArmyEntity(ArmyDTO armyDTO) {
		ArmyEntity armyEntity = new ArmyEntity();
		BeanUtils.copyProperties(armyDTO, armyEntity);

		armyEntity.setFromX(armyDTO.getFrom_x());
		armyEntity.setFromY(armyDTO.getFrom_y());
		armyEntity.setToX(armyDTO.getTo_x());
		armyEntity.setToY(armyDTO.getTo_y());

		if (armyDTO.getGenerals() != null) {
			String generalStr = JSONArray.toJSONString(armyDTO.getGenerals());
			armyEntity.setGenerals(generalStr);
		}

		if (armyDTO.getSoldiers() != null) {
			String soldierStr = JSONArray.toJSONString(armyDTO.getSoldiers());
			armyEntity.setSoldiers(soldierStr);
		}

		if (armyDTO.getCon_cnts() != null) {
			String conCntsStr = JSONArray.toJSONString(armyDTO.getCon_cnts());
			armyEntity.setConscriptCnts(conCntsStr);
		}

		if (armyDTO.getCon_times() != null) {
			String conTimeStr = JSONArray.toJSONString(armyDTO.getCon_times());
			armyEntity.setConscriptTimes(conTimeStr);
		}

		return armyEntity;
	}

	private void addArmy2Cache(WarArmyDTO armyDTO) {
		// 按DB id
		dbArmyMap.put(armyDTO.getArmy().getId(), armyDTO);

		// 按城市id
		cityArmyMap.computeIfAbsent(armyDTO.getArmy().getCityId(), k -> new ArrayList<>()).add(armyDTO);

		// 按角色id 和 order
		roleOrderArmyMap.computeIfAbsent(armyDTO.getArmy().getRid(), k -> new ConcurrentHashMap<>())
				.put(armyDTO.getArmy().getOrder(), armyDTO);
	}

	private synchronized boolean saveArmy2DB(ArmyDTO armyDTO) {
		ArmyEntity armyEntity = armyDTO2ArmyEntity(armyDTO);
		boolean ret = armyService.save(armyEntity);
		armyDTO.setId(armyEntity.getId());
		return ret;
	}

	/******************************************/
	/*************** init end *****************/
	/******************************************/

	public Map<Integer, WarArmyDTO> getArmyAll() {
		return dbArmyMap;
	}


	/**
	 * 获取NPC军队 配置
	 *
	 * @param cellLevel
	 * @return
	 */
	public List<WarArmyDTO> getNpcArmyList(Integer cellLevel) {
		return npcArmyConfigMap.get(cellLevel);
	}

	/**
	 * 获取军队
	 *
	 * @param aid
	 * @return
	 */
	public WarArmyDTO getArmy(Integer aid) {
		return dbArmyMap.get(aid);
	}

	/**
	 * 获取角色的军队
	 *
	 * @param rid
	 * @return
	 */
	public List<WarArmyDTO> getRoleArmy(Integer rid) {
		return Optional.ofNullable(roleOrderArmyMap)
				.map(map -> map.getOrDefault(rid, Map.of()))
				.orElse(null)
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	/**
	 * 获取城市的军队
	 *
	 * @param cid
	 * @return
	 */
	public List<WarArmyDTO> getCityArmy(Integer cid) {
		return cityArmyMap.computeIfAbsent(cid, k -> new ArrayList<>());
	}

	/**
	 * 获取移动中，并且到达时间的军团
	 *
	 * @param time
	 * @return
	 */
	public List<WarArmyDTO> getEndTimeArmy(Integer time) {
		return endTimeArmyMap.computeIfAbsent(time, k -> new ArrayList<>());
	}

	/**
	 * 获取移动中，并且到达时间的军团
	 *
	 * @return
	 */
	public Map<Integer, List<WarArmyDTO>> getEndTimeArmyAll() {
		return endTimeArmyMap;
	}

	/**
	 * 添加移动中，并且到达时间的军团
	 *
	 * @param time
	 * @return
	 */
	public void addEndTimeArmy(Integer time, WarArmyDTO army) {
		endTimeArmyMap.computeIfAbsent(time, k -> new ArrayList<>()).add(army);
	}

	/**
	 * 添加移动中，并且到达时间的军团
	 *
	 * @param time
	 * @return
	 */
	public void removEndTimeArmy(Integer time) {
		endTimeArmyMap.remove(time);
	}

	/**
	 * 获取城外的军团
	 *
	 * @return
	 */
	public Map<Integer, List<WarArmyDTO>> getOutArmyMap() {
		return outArmyMap;
	}


	/**
	 * 获取城外的军团
	 *
	 * @param aid
	 * @return
	 */
	public List<WarArmyDTO> getOutArmy(Integer aid) {
		return outArmyMap.computeIfAbsent(aid, k -> new ArrayList<>());
	}

	/**
	 * 添加城外的军团
	 *
	 * @param cid
	 * @param army
	 */
	public void addOutArmy(Integer cid, WarArmyDTO army) {
		outArmyMap.computeIfAbsent(cid, k -> new ArrayList<>()).add(army);
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @return
	 */
	public void setPassByPosArmyMap(Map<Integer, Map<Integer, WarArmyDTO>> passByPosArmyMap) {
		this.passByPosArmyMap = passByPosArmyMap;
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @return
	 */
	public Map<Integer, List<WarArmyDTO>> getStopInPosArmyMap() {
		return stopInPosArmyMap;
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param pos
	 * @return
	 */
	public List<WarArmyDTO> getStopInPosArmyMap(PositionDTO pos) {
		Integer posId = CityPositionUtils.position2Number(pos);
		return stopInPosArmyMap.computeIfAbsent(posId, k -> new ArrayList<>());
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param posId
	 * @return
	 */
	public List<WarArmyDTO> getStopInPosArmyMap(Integer posId) {
		return stopInPosArmyMap.computeIfAbsent(posId, k -> new ArrayList<>());
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param army
	 * @return
	 */
	public void add2StopInPosArmyMap(WarArmyDTO army) {
		Integer posId = CityPositionUtils.position2Number(army.getArmy().getTo_x(), army.getArmy().getTo_y());
		stopInPosArmyMap.computeIfAbsent(posId, k -> new ArrayList<>()).add(army);
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param posId
	 * @return
	 */
	public void delFromStopInPosArmyMap(Integer posId) {
		stopInPosArmyMap.remove(posId);
	}

	/**
	 * 获取城市的军队
	 *
	 * @param cid
	 * @return
	 */
	public List<WarArmyDTO> getCityArmyWar(Integer cid) {
		List<WarArmyDTO> armys = cityArmyMap.computeIfAbsent(cid, k -> new ArrayList<>());

		if (armys.size() == 0) {
			return null;
		}

		List<WarArmyDTO> npcArmys = new ArrayList<>();
		armys.forEach(army -> {
			npcArmys.add(army);
		});

		return npcArmys;
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param posX
	 * @param posY
	 * @return
	 */
	public List<WarArmyDTO> getStopInPosArmyListWar(Integer posX, Integer posY) {
		Integer posId = CityPositionUtils.position2Number(posX, posY);
		List<WarArmyDTO> armys = stopInPosArmyMap.computeIfAbsent(posId, k -> new ArrayList<>());
		if (armys.size() == 0) {
			return null;
		}

		List<WarArmyDTO> npcArmys = new ArrayList<>();
		armys.forEach(army -> {
			npcArmys.add(army);
		});

		return npcArmys;
	}

	public WarArmyDTO newWarArmy(ArmyDTO army) {
		WarArmyDTO warArmy = new WarArmyDTO();
		warArmy.setArmy(army);

		List<GeneralDTO> generalList = new ArrayList<>();
		army.getGenerals().forEach(gid -> {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			generalList.add(general);
		});

		warArmy.setGeneralList(generalList);

		return warArmy;
	}

	/**
	 * 添加Pos的军团
	 *
	 * @param posId
	 * @param armys
	 */
	public void putStopInPosArmys(Integer posId, List<WarArmyDTO> armys) {
		stopInPosArmyMap.put(posId, armys);
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @return
	 */
	public Map<Integer, List<WarArmyDTO>> getStopInPosNPCArmyList() {
		return stopInPosNPCArmyMap;
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param posX, posY
	 * @return
	 */
	public List<WarArmyDTO> getStopInPosNPCArmyList(Integer posX, Integer posY) {
		Integer posId = CityPositionUtils.position2Number(posX, posY);
		return stopInPosNPCArmyMap.computeIfAbsent(posId, k -> new ArrayList<>());
	}

	/**
	 * 添加Pos的军团
	 *
	 * @param posId
	 * @param armys
	 */
	public void putStopInPosNPCArmys(Integer posId, List<WarArmyDTO> armys) {
		stopInPosNPCArmyMap.put(posId, armys);
	}

	/**
	 * 添加Pos的军团
	 *
	 * @param posId
	 * @param army
	 */
	public void add2StopInPosNPCArmys(Integer posId, WarArmyDTO army) {
		stopInPosNPCArmyMap.computeIfAbsent(posId, k -> new ArrayList<>()).add(army);
	}

	/**
	 * 删除pos的军团
	 *
	 * @param posId
	 */
	public void deleteStopInPosNPCArmys(Integer posId) {
		stopInPosNPCArmyMap.remove(posId);
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @return
	 */
	public Map<Integer, Map<Integer, WarArmyDTO>> getPassByPosArmyMap() {
		return passByPosArmyMap;
	}

	/**
	 * 玩家停留位置的军队
	 *
	 * @param posId
	 * @return
	 */
	public Map<Integer, WarArmyDTO> getPassByPosArmys(Integer posId) {
		return passByPosArmyMap.computeIfAbsent(posId, k -> new ConcurrentHashMap<>());
	}

	/**
	 * 添加城外的军团
	 *
	 * @param posId
	 * @param army
	 */
	public void addPassByPosArmy(Integer posId, WarArmyDTO army) {
		passByPosArmyMap.computeIfAbsent(posId, k -> new ConcurrentHashMap<>()).put(army.getArmy().getId(), army);
	}


	/**
	 * 获取军队
	 *
	 * @param cid
	 * @param order
	 * @return
	 */
	public WarArmyDTO getOrCreateArmy(Integer rid, Integer cid, Integer order) {
		WarArmyDTO armyDTO = roleOrderArmyMap.computeIfAbsent(rid, k -> new ConcurrentHashMap<>())
				.computeIfAbsent(order, k -> createArmy(rid, cid, order));

		// 新数据需要更新DB及其它缓存
		if (armyDTO.getArmy().getId() == null || armyDTO.getArmy().getId().equals(0)) {
			// 先保存到DB，获取db id
			saveArmy2DB(armyDTO.getArmy());

			// 更新其它缓存
			// 按DB id
			dbArmyMap.put(armyDTO.getArmy().getId(), armyDTO);

			// 按城市id
			cityArmyMap.computeIfAbsent(armyDTO.getArmy().getCityId(), k -> new ArrayList<>()).add(armyDTO);
		}

		return armyDTO;
	}

	public void clearConscript(ArmyDTO army) {
		if (army.getCmd() == ArmyCmd.ArmyCmdConscript.getValue()) {
			for (int i = 0; i < army.getCon_cnts().size(); i++) {
				army.getCon_cnts().set(i, 0);
				army.getCon_times().set(i, 0);
			}
			army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
		}
	}

	private WarArmyDTO createArmy(Integer rid, Integer cid, Integer order) {
		Integer[] initVaue = {0, 0, 0};
		return createArmy(rid, cid, order, Arrays.asList(initVaue), Arrays.asList(initVaue));
	}

	private WarArmyDTO createArmy(Integer rid, Integer cid, Integer order, List<Integer> generals, List<Integer> soldiers) {
		ArmyDTO armyDTO = new ArmyDTO();

		armyDTO.setId(0);
		armyDTO.setRid(rid);
		armyDTO.setCityId(cid);
		armyDTO.setOrder(order);
		armyDTO.setGenerals(generals);
		armyDTO.setSoldiers(soldiers);

		Integer[] array = {0, 0, 0};
		armyDTO.setCon_cnts(Arrays.asList(array));
		armyDTO.setCon_times(Arrays.asList(array));

		CityDTO city = cityMgr.getCity(cid);
		if (city != null) {
			armyDTO.setFromTo(city.getX(), city.getY(), city.getX(), city.getY());
		}

		return newWarArmy(armyDTO);
	}

	/**
	 * 更新军队,
	 * 注意入参仅包含更新部分内容
	 *
	 * @param updateArmy
	 * @return
	 */

	public boolean updateArmy2DB(ArmyDTO updateArmy) {
		ArmyEntity armyEntity = armyDTO2ArmyEntity(updateArmy);


		UpdateWrapper<ArmyEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", armyEntity.getId());

		return armyService.update(armyEntity, updateWrapper);
	}

	/**
	 * 军队是否可以出征
	 *
	 * @param army
	 * @return
	 */
	public boolean canGoTOWar(ArmyDTO army) {
		return army.getGenerals().get(0) != 0 && army.getCmd() == ArmyCmd.ArmyCmdIdle.getValue();
	}

	/**
	 * 体力是否足够
	 *
	 * @param army
	 * @param cost
	 * @return
	 */
	public boolean physicalPowerIsEnough(ArmyDTO army, int cost) {
		for (Integer gid : army.getGenerals()) {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			if (general == null) {
				continue;
			}
			if (general.getPhysical_power() < cost) {
				return false;
			}
		}

		return true;
	}

	public boolean tryUsePhysicalPower(ArmyDTO army, int cost) {

		if (!physicalPowerIsEnough(army, cost)) {
			return false;
		}

		for (Integer gid : army.getGenerals()) {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			if (general == null) {
				continue;
			}
			general.setPhysical_power(general.getPhysical_power() - cost);
		}

		// TODO 更新 DB

		return true;
	}

	/**
	 * 计算速度
	 * 速度 = 最慢将领的速度 + 阵营加成
	 *
	 * @param army
	 * @return
	 */
	public Integer countSpeed(ArmyDTO army) {
		Integer speed = 100000;
		for (Integer gid : army.getGenerals()) {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			if (general == null) {
				continue;
			}
			GeneralList generalConfig = jsonConfigMgr.getGeneralConfig().getGeneralMap().get(general.getCfgId());
			Integer gSpeed = generalConfig.getSpeed() + generalConfig.getSpeed_grow() * general.getSpeed_added();
			// 找出最慢的将领
			if (gSpeed < speed) {
				speed = gSpeed;
			}
		}

		//阵营加成
		Integer camp = getCamp(army);
		List<Integer> campAdds = new ArrayList<>();
		if (camp > 0) {
			// TODO 看读不懂 FacilityAdditionType.TypeHanAddition.getValue() - 1 + camp  是啥意思, 加成类型？
			campAdds.addAll(cityFacilityMgr.getAdditions(army.getCityId(),
					FacilityAdditionType.TypeHanAddition.getValue() - 1 + camp));
		}

		if (campAdds.size() > 0) {
			return speed + campAdds.get(0);
		}

		return speed;
	}

	/**
	 * 归属于该位置的军队数量
	 *
	 * @param rid
	 * @param position
	 * @return
	 */
	public Integer belongPosArmyCnt(Integer rid, PositionDTO position) {
		int cnt = 0;
		List<WarArmyDTO> armys = getRoleArmy(rid);

		if (armys == null || armys.size() == 0) {
			return cnt;
		}

		for (WarArmyDTO army : armys) {
			if (PositionUtils.equalsPosition(army.getArmy().getFrom_x(), army.getArmy().getFrom_y(), position) ||
					(army.getArmy().getCmd() == ArmyCmd.ArmyCmdTransfer.getValue() &&
							PositionUtils.equalsPosition(army.getArmy().getTo_x(), army.getArmy().getTo_y(), position))) {
				cnt += 1;
			}
		}

		return cnt;
	}


	//获取军队阵营
	public Integer getCamp(ArmyDTO army) {
		Integer camp = 0;
		for (Integer gid : army.getGenerals()) {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			if (general == null) {
				return camp;
			}

			Integer generalConfigCamp = generalMgr.getCamp(general.getCfgId());

			if (camp == 0) {
				camp = generalConfigCamp;
			} else {
				if (camp != generalConfigCamp) {
					return 0;
				}
			}
		}

		return camp;
	}

	public PositionDTO countCurrentPosition(ArmyDTO army) {
		Long diffTime = army.getEnd() - army.getStart();
		Long passTime = System.currentTimeMillis() / 1000 - army.getStart();
		Double rate = (passTime.doubleValue() / diffTime);
		PositionDTO curPos;
		if (army.getCmd() == ArmyCmd.ArmyCmdBack.getValue()) {
			curPos = PositionUtils.ratePosition(army.getFrom_x(), army.getFrom_y(), army.getTo_x(), army.getTo_y(), rate);
		} else {
			curPos = PositionUtils.ratePosition(army.getTo_x(), army.getTo_y(), army.getFrom_x(), army.getFrom_y(), rate);
		}

		curPos.setX(Math.min(Math.max(curPos.getX(), 0), CityPositionUtils.MapWidth));
		curPos.setY(Math.min(Math.max(curPos.getY(), 0), CityPositionUtils.MapHeight));

		return curPos;
	}

	/**
	 * 军队总损伤
	 *
	 * @param army
	 * @return
	 */
	public Integer getDestroy(ArmyDTO army) {
		Integer destroy = 0;
		for (Integer gid : army.getGenerals()) {
			GeneralDTO general = generalMgr.getRoleGeneral(gid);
			destroy += generalMgr.getDestroy(general);
		}
		return destroy;
	}

	public Float getHarmRatio(Integer attId, Integer defId) {

		General_armsArms attArm = jsonConfigMgr.getGeneralArmConfig(attId);

		if (attArm != null && jsonConfigMgr.getGeneralArmConfig(defId) != null) {
			return attArm.getHarm_ratio().get(attId - 1) / 100.0f;
		} else {
			return 1.0f;
		}
	}
}
