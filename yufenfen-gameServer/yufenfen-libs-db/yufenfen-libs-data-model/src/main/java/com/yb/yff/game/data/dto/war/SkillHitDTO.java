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
	private int fId; // 发起的id
	private boolean fIsA; // 发起方是否攻城方, true-攻城方，false-守城方
	private boolean tIsA; // 作用方是否攻城方, true-攻城方，false-守城方
	private List<Integer> tId; // 作用目标id
	private int cId; // 技能配置id
	private int lv; // 技能等级
	private List<Integer> iE; // 技能包括的效果
	private List<Integer> eV; // 效果值
	private List<Integer> eR; // 效果持续回合数
	private List<Integer> kill; // 技能杀死数量
}
