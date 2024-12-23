package com.yb.yff.game.data.dto.skill;

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
 * @Class: SkillLevelDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：技能
 */
@Data
public class SkillLevelDTO {
	private  Integer id;
	private  Integer lv;
	private  Integer cfgId;

	public SkillLevelDTO() {
	}

	public SkillLevelDTO(Integer id, Integer lv, Integer cfgId) {
		this.id = id;
		this.lv = lv;
		this.cfgId = cfgId;
	}
}
