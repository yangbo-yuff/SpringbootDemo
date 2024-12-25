package com.yb.yff.game.data.dto.war;

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
 * @Class: HitDTO
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 攻击 数据
 */
@Data
public class HitDTO {
	private int aId; // 本回合发起攻击的武将id
	private int dId; // 本回合防御方的武将id
	private int dLoss; // 本回合防守方损失的兵力
	private List<SkillHitDTO> aBs; // 攻击方攻击前技能
	private List<SkillHitDTO> aAs; // 攻击方攻击后技能
	private List<SkillHitDTO> dAs; // 防守方被攻击后触发技能

	public HitDTO() {
		this.aBs = new ArrayList<>();
		this.aAs = new ArrayList<>();
		this.dAs = new ArrayList<>();
	}
}
