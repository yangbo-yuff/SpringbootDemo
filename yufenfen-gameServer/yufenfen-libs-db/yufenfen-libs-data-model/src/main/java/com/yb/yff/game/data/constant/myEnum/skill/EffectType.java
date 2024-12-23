package com.yb.yff.game.data.constant.myEnum.skill;

import com.yb.yff.game.data.constant.EnumUtils;

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
 * @Class: TriggerType
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 效果类型
 */
public enum EffectType implements EnumUtils<Integer> {
	HurtRate(1),    // 伤害率
	Force(2),       // 攻击
	Defense(3),     // 防御
	Strategy(4),    // 谋略
	Speed(5),       // 速度
	Destroy(6);     // 破坏

	private final int value;

	EffectType(int value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	@Override
	public Integer getValue() {
		return value;
	}

}
