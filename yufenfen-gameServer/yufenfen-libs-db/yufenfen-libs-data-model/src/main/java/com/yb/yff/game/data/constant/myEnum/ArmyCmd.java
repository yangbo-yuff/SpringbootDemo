package com.yb.yff.game.data.constant.myEnum;

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
 * @Class: GeneralState
 * @CreatedOn 2024/11/8.
 * @Email: yangboyff@gmail.com
 * @Description: 军队指令
 */
public enum ArmyCmd implements EnumUtils<Integer> {
	// 空闲
	ArmyCmdIdle(0),

	// 攻击
	ArmyCmdAttack(1),

	// 驻军
	ArmyCmdDefend(2),

	// 屯垦
	ArmyCmdReclamation(3),

	// 撤退
	ArmyCmdBack(4),

	// 征兵
	ArmyCmdConscript(5),

	// 调动
	ArmyCmdTransfer(6);

	private final int value;

	ArmyCmd(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
