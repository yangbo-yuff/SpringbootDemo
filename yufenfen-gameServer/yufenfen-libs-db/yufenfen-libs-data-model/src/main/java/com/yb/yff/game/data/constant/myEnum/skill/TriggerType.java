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
 * @Description: 触发类型
 */
public enum TriggerType implements EnumUtils<Integer> {
	positive(1), //主动
	passive(2),  //被动
	addAttack(3), //追击
	command(4); //指挥

	private final int value;

	TriggerType(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
