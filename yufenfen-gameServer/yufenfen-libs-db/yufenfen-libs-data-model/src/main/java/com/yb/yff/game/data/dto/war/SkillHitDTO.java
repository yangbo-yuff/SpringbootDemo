package com.yb.yff.game.data.dto.war;

import lombok.Data;

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
 * @Class: SkillHitDTO
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 技能 攻击 数据
 */
@Data
public class SkillHitDTO {
	private int f_id; // 发起的id
	private List<Integer> t_id; // 作用目标id
	private int c_id; // 技能配置id
	private int lv; // 技能等级
	private List<Integer> i_e; // 技能包括的效果
	private List<Integer> e_v; // 效果值
	private List<Integer> e_r; // 效果持续回合数
	private List<Integer> kill; // 技能杀死数量
}
