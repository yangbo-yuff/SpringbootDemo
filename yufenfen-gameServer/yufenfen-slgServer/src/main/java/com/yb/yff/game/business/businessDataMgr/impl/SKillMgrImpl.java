package com.yb.yff.game.business.businessDataMgr.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.skill.SkillDTO;
import com.yb.yff.game.data.dto.skill.SkillLevelDTO;
import com.yb.yff.game.data.entity.SkillEntity;
import com.yb.yff.game.jsondb.data.dto.skill.Skill;
import com.yb.yff.game.service.ISkillService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * @Class: SKillMgrImpl
 * @CreatedOn 2024/12/19.
 * @Email: yangboyff@gmail.com
 * @Description: 技能
 */
@Component
@Slf4j
public class SKillMgrImpl implements IJsonDataHandler {
	@Autowired
	ISkillService skillService;

	@Autowired
	JsonConfigMgr jsonConfigMgr;

	/**
	 * key: rid
	 */
	ConcurrentHashMap<Integer, List<SkillDTO>> skillMap = new ConcurrentHashMap<>();


	/**
	 * 同步数据到DB
	 */
	@Override
	public void syncData2DB() {

	}

	public List<SkillDTO> getRoleSkills(Integer rid) {
		return skillMap.computeIfAbsent(rid, k -> getRoleSkillsFromDB(rid));
	}

	public void addRoleSkill2Map(Integer rid, SkillDTO skill) {
		getRoleSkills(rid).add(skill);
	}

	public LogicTaskResultDTO<SkillDTO> getSkillOrCreate(Integer rid, Integer cfgId) {
		LogicTaskResultDTO<SkillDTO> result = new LogicTaskResultDTO<>();

		SkillDTO ret = null;

		List<SkillDTO> m = getRoleSkills(rid);
		if (m != null && m.size() > 0) {
			for (SkillDTO v : m) {
				if (!v.getCfgId().equals(cfgId)) {
					continue;
				} else {
					ret = v;
				}
			}
		}

		if (ret == null) {
			ret = createSkill(rid, cfgId);
			if (ret == null) {
				result.setCode(NetResponseCodeConstants.DBError);
			}
		}

		result.setResult(ret);

		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;

	}

	/**
	 * 检测技能的兵种
	 *
	 * @param armId
	 * @param cfgId
	 * @return
	 */
	public boolean checkSKillsArmy(Integer armId, Integer cfgId) {
		List<Integer> skillArmConfig = jsonConfigMgr.getSkillArmConfig(cfgId);
		for (Integer cfgArmId : skillArmConfig) {
			if (cfgArmId.equals(armId)) {
				return true;
			}
		}

		return false;
	}

	public ResponseCode checkSkillLevel(SkillLevelDTO gSkill) {
		Skill skillCfg = jsonConfigMgr.getSkillConfig(gSkill.getCfgId());
		if (skillCfg == null) {
			return NetResponseCodeConstants.PosNotSkill;
		}

		if (gSkill.getLv() > skillCfg.getLevels().size()) {
			return NetResponseCodeConstants.SkillLevelFull;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	private SkillDTO createSkill(Integer rid, Integer cfgId) {
		SkillDTO skillDTO = new SkillDTO(rid, cfgId);
		SkillEntity entity = skillDTO2Entity(skillDTO);
		Integer insertRes = skillService.getBaseMapper().insert(entity);
		if (insertRes == 0) {
			return null;
		}

		skillDTO.setId(entity.getId());

		addRoleSkill2Map(rid, skillDTO);

		return skillDTO;
	}

	public boolean saveOrUpdateSkill2DB(SkillDTO skillDTO) {
		SkillEntity entity = skillDTO2Entity(skillDTO);
		try {
			return skillService.saveOrUpdate(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private List<SkillDTO> getRoleSkillsFromDB(Integer rid) {
		QueryWrapper<SkillEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rid", rid);

		try {
			List<SkillEntity> dbList = skillService.list(queryWrapper);

			List<SkillDTO> skills = new ArrayList<>();

			dbList.forEach(skillEntity -> {
				SkillDTO skillLevelDTO = skillEntity2DTO(skillEntity);
				skills.add(skillLevelDTO);
			});

			return skills;
		} catch (Exception e) {
			log.error("getAllSkill error", e);
			return null;
		}
	}

	private SkillDTO skillEntity2DTO(SkillEntity entity) {
		SkillDTO dto = new SkillDTO(entity.getId(), entity.getRid(), entity.getCfgId());
		if(StringUtil.isNullOrEmpty(entity.getBelongGenerals())){
			return dto;
		}

		List<Integer> generals = JSONArray.parseArray(entity.getBelongGenerals(), Integer.class);
		dto.setGenerals(generals);

		return dto;
	}

	private SkillEntity skillDTO2Entity(SkillDTO dto) {
		SkillEntity entity = new SkillEntity();
		BeanUtils.copyProperties(dto, entity);

		if(dto.getGenerals() == null || dto.getGenerals().size() == 0){
			return entity;
		}

		entity.setBelongGenerals(JSONArray.toJSONString(dto.getGenerals()));

		return entity;
	}
}
