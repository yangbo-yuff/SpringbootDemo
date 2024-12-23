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
	private int a_id; // 本回合发起攻击的武将id
	private int d_id; // 本回合防御方的武将id
	private int d_loss; // 本回合防守方损失的兵力
	private List<SkillHitDTO> a_bs; // 攻击方攻击前技能
	private List<SkillHitDTO> a_as; // 攻击方攻击后技能
	private List<SkillHitDTO> d_as; // 防守方被攻击后触发技能

	public HitDTO() {
		this.a_bs = new ArrayList<>();
		this.a_as = new ArrayList<>();
		this.d_as = new ArrayList<>();
	}
}
