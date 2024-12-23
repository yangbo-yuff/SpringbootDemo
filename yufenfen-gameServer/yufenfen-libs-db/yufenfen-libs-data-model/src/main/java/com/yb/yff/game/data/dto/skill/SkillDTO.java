package com.yb.yff.game.data.dto.skill;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
public class SkillDTO {
	private Integer id;
	private Integer rid;
	private Integer cfgId;
	private List<Integer> generals;

	public SkillDTO() {}
	public SkillDTO(Integer rid, Integer cfgId) {
		this.rid = rid;
		this.cfgId = cfgId;
		this.generals = new ArrayList<>();
	}

	public SkillDTO(Integer id, Integer rid, Integer cfgId) {
		this.id = id;
		this.rid = rid;
		this.cfgId = cfgId;
		this.generals = new ArrayList<>();
	}
}
