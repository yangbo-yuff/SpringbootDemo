package com.yb.yff.game.business.businessDataMgr.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.constant.myEnum.GeneralState;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.dto.skill.SkillLevelDTO;
import com.yb.yff.game.data.entity.GeneralEntity;
import com.yb.yff.game.jsondb.data.dto.general.GeneralList;
import com.yb.yff.game.jsondb.data.dto.general.General_basicLevels;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_armyArmysArmy;
import com.yb.yff.game.service.IGeneralService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * @Description: 军队管理
 */
@Component
@Slf4j
public class GeneralMgrImpl implements IJsonDataHandler {
	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	RoleDataMgrImpl gameRoleDataMgrImpl;

	@Autowired
	IGeneralService generalService;

	// key:armyId
	ConcurrentHashMap<Integer, GeneralDTO> dbGeneralMap = new ConcurrentHashMap<>();

	// key:cityId
	ConcurrentHashMap<Integer, List<GeneralDTO>> cityGeneralMap = new ConcurrentHashMap<>();

	// key:rid
	ConcurrentHashMap<Integer, List<GeneralDTO>> roleGeneralMap = new ConcurrentHashMap<>();

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

		if (dbGeneralMap.size() > 0) {
			return;
		}

		// 查询数据库中是否有记录，没有则不需要重数据库缓存将领数据
		if (generalService.count() == 0) {
			return;
		}

		//  缓存 将领数据
		QueryWrapper<GeneralEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("state", GeneralState.GeneralConvert.getValue());
		queryWrapper.ne("rid", 0);

		List<GeneralEntity> dbList = generalService.getBaseMapper().selectList(queryWrapper);
		addGenerals2Cache(dbList);
	}

	/**
	 * 批量添加将领
	 *
	 * @param generalEntities
	 */
	private void addGenerals2Cache(List<GeneralEntity> generalEntities) {

		generalEntities.forEach(generalEntity -> {
			GeneralDTO general = generalEntity2DTO(generalEntity);

			addGeneral2Cache(general);
		});
	}

	/**
	 * 添加将领   注意，切莫分开处理，内存数据要一致
	 *
	 * @param general
	 */
	private synchronized void addGeneral2Cache(GeneralDTO general) {
		// 按DB id
		dbGeneralMap.put(general.getId(), general);

		// 按城市id
		cityGeneralMap.computeIfAbsent(general.getCityId(), k -> new ArrayList<>()).add(general);

		// 按角色id
		roleGeneralMap.computeIfAbsent(general.getRid(), k -> new ArrayList<>()).add(general);
	}

	/**
	 * 删除将领,  注意，切莫分开处理，内存数据要一致
	 *
	 * @param general
	 */
	private void removeGeneralFromCache(GeneralDTO general) {

		dbGeneralMap.remove(general.getId());

		List<GeneralDTO> getCityGenerals = cityGeneralMap.computeIfAbsent(general.getCityId(), k -> new ArrayList<>());
		for (GeneralDTO generalDTO : getCityGenerals) {
			if (generalDTO.getId() == general.getId()) {
				getCityGenerals.remove(generalDTO);
				break;
			}
		}

		List<GeneralDTO> roleGenerals = roleGeneralMap.computeIfAbsent(general.getRid(), k -> new ArrayList<>());
		for (GeneralDTO generalDTO : roleGenerals) {
			if (generalDTO.getId() == general.getId()) {
				roleGenerals.remove(generalDTO);
				break;
			}
		}
	}

	/******************************************/
	/*************** init end *****************/
	/******************************************/

	/**
	 * 批量查询将领
	 *
	 * @param rids
	 * @return
	 */
	public List<GeneralDTO> selectGenerals(List<Integer> rids) {
		List<GeneralDTO> selects = new ArrayList<>();

		rids.forEach(rid -> {
			if (rid == null) {
				return;
			}
			GeneralDTO general = dbGeneralMap.get(rid);
			if (general == null) {
				return;
			}
			selects.add(general);
		});

		return selects;
	}

	/**
	 * 更新将领状态
	 *
	 * @param general
	 * @return
	 */
	public boolean updateGeneralState(GeneralDTO general) {
		// 更新到缓存
		dbGeneralMap.get(general.getId()).setState(general.getState());
		// 如果是流放将领，则删除缓存
		if (general.getState() == GeneralState.GeneralConvert.getValue()) {
			removeGeneralFromCache(general);
		}

		// 更新到DB
		GeneralEntity generalEntity = new GeneralEntity();
		generalEntity.setId(general.getId());
		generalEntity.setState(general.getState());

		UpdateWrapper<GeneralEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", general.getId())
				.set("state", general.getState());

		return generalService.update(updateWrapper);
	}

	/**
	 * 更新将领到DB
	 * 注意，入参，仅包含更新的字段
	 *
	 * @param general
	 * @return
	 */
	public boolean updateGeneral2DB(GeneralDTO general) {

		GeneralEntity generalEntity = generalDTO2Entity(general);

		UpdateWrapper<GeneralEntity> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", generalEntity.getId());

		return generalService.update(generalEntity, updateWrapper);
	}
//	generalService.updateById(generalEntity);

	/**
	 * 获取角色的将领
	 *
	 * @param gid
	 * @return
	 */
	public GeneralDTO getRoleGeneral(Integer gid) {
		return dbGeneralMap.get(gid);
	}

	/**
	 * 获取角色的将领
	 *
	 * @param rid
	 * @return
	 */
	public List<GeneralDTO> getRoleGenerals(Integer rid) {
		return roleGeneralMap.computeIfAbsent(rid, k -> new ArrayList<>());
	}

	/**
	 * 获取城市的将领
	 *
	 * @param cid
	 * @return
	 */
	public List<GeneralDTO> getCityGenerals(Integer cid) {
		return cityGeneralMap.computeIfAbsent(cid, k -> new ArrayList<>());
	}

	/**
	 * 获取将领配置列表 中 将领的ID
	 *
	 * @return
	 */
	public List<Integer> getGCGidlList() {
		return jsonConfigMgr.getGeneralConfig().getGeneralList();
	}

	/**
	 * 获取将领配置列表 中 将领的带兵数
	 *
	 * @return
	 */
	public Integer getGCSoldierNum(Integer level) {
		if (jsonConfigMgr.getGeneralConfig().getLevelMap().size() < level) {
			return 0;
		}

		return jsonConfigMgr.getGeneralConfig().getLevelMap().get(level).getSoldiers();
	}


	public void expToLevel(GeneralDTO general) {
		Integer exp = general.getExp();
		int level = 0;
		int levelSize = jsonConfigMgr.getGeneralConfig().getLevelMap().size();
		int limitExp = jsonConfigMgr.getGeneralConfig().getLevelMap().get(levelSize -1).getExp();

		for ( Map.Entry<Integer, General_basicLevels> entry : jsonConfigMgr.getGeneralConfig().getLevelMap().entrySet()) {
			General_basicLevels gBasiceLevelConfig = entry.getValue();
			if (exp >= gBasiceLevelConfig.getExp() && gBasiceLevelConfig.getLevel() > level) {
				level = gBasiceLevelConfig.getLevel();
			}
		}

		exp = Math.min(limitExp, exp);

		general.setLevel(level);
		general.setExp(exp);
	}



	/**
	 * 批量生成将领
	 *
	 * @param rid
	 * @param cfgIdList
	 * @return
	 */
	@Transactional
	public synchronized List<GeneralDTO> createGeneral(Integer rid, Integer cid, List<Integer> cfgIdList) {

		List<GeneralEntity> generalEntities = createNewGeneralByConfig(rid, cid, cfgIdList);

		if (!generalService.saveBatch(generalEntities)) {
			return null;
		}

		List<GeneralDTO> newGenerals = new ArrayList<>();
		generalEntities.forEach(generalEntity -> {
			GeneralDTO general = generalEntity2DTO(generalEntity);
			addGeneral2Cache(general);
			newGenerals.add(general);
		});

		return newGenerals;
	}


	/**
	 * 获取将领的攻击力
	 *
	 * @param general
	 * @return
	 */
	public Integer getDestroy(GeneralDTO general) {
		if (general == null) {
			return 0;
		}

		Integer cfgId = general.getCfgId();
		GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(cfgId);

		return generalConfig.getDestroy() + generalConfig.getDestroy_grow() * general.getLevel() + general.getDestroyAdded();
	}

	public Integer getSpeed(GeneralDTO general) {
		if (general == null) {
			return 0;
		}

		GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(general.getCfgId());

		return generalConfig.getSpeed() + generalConfig.getSpeed_grow() * general.getLevel() + general.getSpeedAdded();
	}

	public Integer getForce(GeneralDTO general) {
		if (general == null) {
			return 0;
		}

		GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(general.getCfgId());

		return generalConfig.getForce() + generalConfig.getForce_grow() * general.getLevel() + general.getForceAdded();
	}

	public Integer getDefense(GeneralDTO general) {
		if (general == null) {
			return 0;
		}

		GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(general.getCfgId());

		return generalConfig.getDefense() + generalConfig.getDefense_grow() * general.getLevel()
				+ general.getDefenseAdded();
	}

	public Integer getStrategy(GeneralDTO general) {
		if (general == null) {
			return 0;
		}

		GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(general.getCfgId());

		return generalConfig.getStrategy() + generalConfig.getStrategy_grow() * general.getLevel()
				+ general.getStrategyAdded();
	}

	//获取阵营
	public Integer getCamp(Integer cfgId) {
		return jsonConfigMgr.getGeneralCampConfig(cfgId);
	}

	public List<GeneralDTO> createNPCGenerals(Npc_armyArmysArmy armyCfg) {
		List<GeneralDTO> npcGenerals = new ArrayList<>();

		for (int i = 0; i < armyCfg.getCfgIds().size(); i++) {
			int cfgId = armyCfg.getCfgIds().get(i);
			GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(cfgId);
			GeneralDTO general = generalConfig2DTO(0, 0, generalConfig);
			npcGenerals.add(general);
		}

		return npcGenerals;
	}

	public List<Integer> getGeneralValue(GeneralDTO general){
		List<Integer> allValue = new ArrayList<>();

		allValue.add(general.getId());
		allValue.add(general.getCfgId());
		allValue.add(general.getPhysicalPower());
		allValue.add(general.getOrder());
		allValue.add(general.getLevel());
		allValue.add(general.getExp());
		allValue.add(general.getCfgId());
		allValue.add(general.getArms());
		allValue.add(general.getHasPrPoint());
		allValue.add(general.getUsePrPoint());
		allValue.add(general.getAttackDistance());
		allValue.add(general.getForceAdded());
		allValue.add(general.getStrategyAdded());
		allValue.add(general.getSpeedAdded());
		allValue.add(general.getDefenseAdded());
		allValue.add(general.getDestroyAdded());
		allValue.add(general.getStarLv());
		allValue.add(general.getStar());

		return allValue;
	}

	/**
	 * 根据将将领配置批量生成将领实体
	 * 用于将领十抽
	 *
	 * @param cfgIdList
	 * @return
	 */
	private List<GeneralEntity> createNewGeneralByConfig(Integer rid, Integer cityId, List<Integer> cfgIdList) {
		List<GeneralEntity> generalEntities = new ArrayList<>();

		cfgIdList.forEach(cfgId -> {
			GeneralList generalConfig = jsonConfigMgr.getGeneralConfig(cfgId);
			GeneralEntity generalEntity = generalConfig2Entity(rid, cityId, generalConfig);

			generalEntities.add(generalEntity);
		});

		return generalEntities;
	}

	/**
	 * 将将领实体转换为将领DTO
	 *
	 * @param generalEntity
	 * @return
	 */
	private GeneralDTO generalEntity2DTO(GeneralEntity generalEntity) {
		GeneralDTO generalDTO = new GeneralDTO();
		BeanUtils.copyProperties(generalEntity, generalDTO);

		if (!StringUtil.isNullOrEmpty(generalEntity.getSkills())) {
			List<SkillLevelDTO> generals = JSONArray.parseArray(generalEntity.getSkills(), SkillLevelDTO.class);
			for (int i = 0; i < generals.size(); i++){
				generalDTO.setPosSkill(i, generals.get(i));
			}
		}

		return generalDTO;
	}

	/**
	 * 将 将领DTO 转换为 将领实体
	 *
	 * @param generalDTO
	 * @return
	 */
	private GeneralEntity generalDTO2Entity(GeneralDTO generalDTO) {

		GeneralEntity generalEntity = new GeneralEntity();
		BeanUtils.copyProperties(generalDTO, generalEntity);

		String skillStr = JSONArray.toJSONString(generalDTO.getSkills());

		generalEntity.setSkills(skillStr);

		return generalEntity;
	}

	/**
	 * 根据配置生成 将领实体
	 *
	 * @param rid
	 * @param cid
	 * @param generalConfig
	 * @return
	 */
	private GeneralEntity generalConfig2Entity(Integer rid, Integer cid, GeneralList generalConfig) {

		GeneralEntity generalEntity = new GeneralEntity();

		generalEntity.setPhysicalPower(jsonConfigMgr.getBasicConfig().getGeneral().getPhysical_power_limit());
		generalEntity.setRid(rid);
		generalEntity.setCityId(cid);
		generalEntity.setCfgId(generalConfig.getCfgId());
		generalEntity.setOrder(0);
		generalEntity.setLevel(1); // 默认1级
		generalEntity.setArms(generalConfig.getArms().get(0)); // 默认选第一个
		generalEntity.setHasPrPoint(0);
		generalEntity.setUsePrPoint(0);
		generalEntity.setAttackDistance(0);
		generalEntity.setForceAdded(0);
		generalEntity.setStrategyAdded(0);
		generalEntity.setDefenseAdded(0);
		generalEntity.setSpeedAdded(0);
		generalEntity.setDestroyAdded(0);
		generalEntity.setStar(generalConfig.getStar());
		generalEntity.setStarLv(0);
		generalEntity.setParentId(0);
		generalEntity.setSkills(null);// 默认技能 没有
		generalEntity.setState(GeneralState.GeneralNormal.getValue());

		return generalEntity;
	}

	/**
	 * 根据配置生成 将领实体
	 *
	 * @param rid
	 * @param cid
	 * @param generalConfig
	 * @return
	 */
	private GeneralDTO generalConfig2DTO(Integer rid, Integer cid, GeneralList generalConfig) {

		GeneralDTO generalDTO = new GeneralDTO();

		generalDTO.setPhysicalPower(jsonConfigMgr.getBasicConfig().getGeneral().getPhysical_power_limit());
		generalDTO.setRid(rid);
		generalDTO.setCityId(cid);
		generalDTO.setCfgId(generalConfig.getCfgId());
		generalDTO.setOrder(0);
		generalDTO.setLevel(1); // 默认1级
		generalDTO.setArms(generalConfig.getArms().get(0)); // 默认选第一个
		generalDTO.setHasPrPoint(0);
		generalDTO.setUsePrPoint(0);
		generalDTO.setStar(generalConfig.getStar());
		generalDTO.setParentId(0);
		generalDTO.setSkills(null);// 默认技能 没有
		generalDTO.setState(GeneralState.GeneralNormal.getValue());

		generalDTO.setExp(0);
		generalDTO.setAttackDistance(0);
		generalDTO.setDefenseAdded(0);
		generalDTO.setDestroyAdded(0);
		generalDTO.setForceAdded(0);
		generalDTO.setPhysicalPower(0);
		generalDTO.setSpeedAdded(0);
		generalDTO.setStarLv(0);
		generalDTO.setStrategyAdded(0);

		return generalDTO;
	}
}
