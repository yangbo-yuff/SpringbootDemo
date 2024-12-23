package com.yb.yff.game.business.businessDataMgr;

import com.yb.yff.game.data.dto.general.GeneralConfigDTO;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigDTO;
import com.yb.yff.game.data.dto.facility.config.FacilitiesDTO;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigLevelDTO;
import com.yb.yff.game.data.dto.nationMap.config.NationConfigDTO;
import com.yb.yff.game.data.dto.nationMap.ConfigDTO;
import com.yb.yff.game.jsondb.data.dto.Basic;
import com.yb.yff.game.jsondb.data.dto.general.GeneralList;
import com.yb.yff.game.jsondb.data.dto.general.General_armsArms;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_army;
import com.yb.yff.game.jsondb.data.dto.npc.Npc_armyArmys;
import com.yb.yff.game.data.dto.city.NationalMaps;
import com.yb.yff.game.jsondb.data.dto.skill.Skill;
import com.yb.yff.game.jsondb.data.dto.skill.Skills;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @Class: JsonConfigMgr
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: Json文件的 配置
 */
@Component
@Slf4j
public class JsonConfigMgr {

	@Value("${game.config.debug:false}")
	private boolean debug;

	NationConfigDTO nationConfig;
	NationalMaps nationConfigMaps;
	Npc_army npcArmyConfig;
	Basic basicConfig;
	FacilitiesDTO facilitiesConfig;
	GeneralConfigDTO generalConfig;
	/**
	 * 技能配置,
	 * key: cfgId
	 */
	private Map<Integer, Skill> skillConfigMap = new HashMap<>();

	/**
	 * 技能配置,
	 * key: armId
	 */
//	private Map<Integer, General_armsArms> generalArmsConfigMap = new HashMap<>();
	public void syncData2Cache(Basic basicConfig, NationalMaps nationConfigMaps, NationConfigDTO nationConfig,
	                           Npc_army npcArmyConfig, FacilitiesDTO facilitiesConfig, GeneralConfigDTO generalConfig,
	                           Skills skillsConfig) {
		this.basicConfig = basicConfig;
		this.nationConfigMaps = nationConfigMaps;
		this.nationConfig = nationConfig;
		this.npcArmyConfig = npcArmyConfig;
		this.facilitiesConfig = facilitiesConfig;
		this.generalConfig = generalConfig;

		skillsConfig.getSkills().forEach(skill -> this.skillConfigMap.put(skill.getCfgId(), skill));

//		generalArmsConfig.getArms().forEach(arms -> generalArmsConfigMap.put(arms.getId(), arms));
	}

	public boolean isDebug() {
		return debug;
	}

	public Basic getBasicConfig() {
		return basicConfig;
	}

	public List<Npc_armyArmys> getNpcArmyConfig() {
		return npcArmyConfig.getArmys();
	}

	public Npc_armyArmys getNpcArmyLevelConfig(Integer level) {
		return npcArmyConfig.getArmys().get(level - 1);
	}


	public NationConfigDTO getNationConfig() {
		return nationConfig;
	}

	public ConfigDTO getBuildConfigByTypeAndLevel(Integer type, Integer level) {
		try {
			return nationConfig.getNmcMap().get(type).get(level);
		} catch (Exception e) {
			return null;
		}
	}

	public MBCustomConfigDTO getMBCustomConfig(Integer type) {
		return nationConfig.getNmccMap().get(type);
	}

	public MBCustomConfigLevelDTO getNationArmyConfig(Integer type, Integer level) {
		try {
			return nationConfig.getNmccMap().get(type).getLevelMap().get(level);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer getNationArmyCntConfig(Integer type, Integer level) {
		try {
			return nationConfig.getNmccMap().get(type).getLevelMap().get(level).getResult().getArmy_cnt();
		} catch (Exception e) {
			return null;
		}
	}

	public NationalMaps getNationalConfigMaps() {
		return nationConfigMaps;
	}

	public FacilitiesDTO getFacilitiesConfig() {
		return facilitiesConfig;
	}

	public GeneralConfigDTO getGeneralConfig() {
		return generalConfig;
	}

	public Skill getSkillConfig(Integer cfgId) {
		return skillConfigMap.get(cfgId);
	}

	/**
	 * 技能对应的兵种列表
	 * @param cfgId
	 * @return
	 */
	public List<Integer> getSkillArmConfig(Integer cfgId) {
		return skillConfigMap.get(cfgId).getArms();
	}

	public General_armsArms getGeneralArmConfig(Integer armId) {
		return generalConfig.getArmMap().get(armId);
	}


	public GeneralList getGeneralConfig(Integer cfgID) {
		return generalConfig.getGeneralMap().get(cfgID);
	}

	public Integer getGeneralCampConfig(Integer cfgID) {
		return generalConfig.getGeneralMap().get(cfgID).getCamp();
	}

	public List<Integer> getFacilitiesConfigAdditions(Integer fType) {
		return facilitiesConfig.getFacilityMap().get(fType).getProperty().getAdditions();
	}

	public List<Integer> getFacilitiesConfigValues(Integer fType, Integer level) {
		return facilitiesConfig.getFacilityMap().get(fType).getProperty().getLeveMap().get(level).getValues();
	}
}
