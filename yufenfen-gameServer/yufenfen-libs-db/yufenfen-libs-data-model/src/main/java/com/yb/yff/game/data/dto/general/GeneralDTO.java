package com.yb.yff.game.data.dto.general;

import com.yb.yff.game.data.dto.skill.SkillLevelDTO;
import lombok.Data;

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
 * @Class: MyGenerals
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：将领
 */
@Data
public class GeneralDTO {
	private Integer id;
	private Integer rid;
	private Integer cityId;
	private Integer cfgId;
	private Integer cost;
	private Integer attackDistance;
	/**
	 * 兵种
	 */
	private Integer arms;
	private Integer defenseAdded;
	private Integer destroyAdded;
	private Integer exp;
	private Integer forceAdded;
	private Integer hasPrPoint;
	private Integer level;
	private Integer order;
	private Integer parentId;
	private Integer physicalPower;
	private SkillLevelDTO [] skills = new SkillLevelDTO[3];
	private Integer speedAdded;
	private Integer star;
	private Integer starLv;
	private Integer state;
	private Integer strategyAdded;
	private Integer usePrPoint;

	public SkillLevelDTO getPosSkill(int pos) {
		return skills[pos];
	}
	public void setPosSkill(int pos, SkillLevelDTO skill) {
		skills[pos] = skill;
	}
}
