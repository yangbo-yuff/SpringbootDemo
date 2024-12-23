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
 * @Description: 目标类型
 */
public enum TargetType implements EnumUtils<Integer> {
	MySelf(1),          //自己
	OurSingle(2),       //我军单体
	OurMostTwo(3),      //我军1-2个目标
	OurMostThree(4),    //我军1-3个目标
	OurAll(5),          //我军全体
	EnemySingle(6),     //敌军单体
	EnemyMostTwo(7),    //敌军1-2个目标
	EnemyMostThree(8),  //我军1-3个目标
	EnemyAll(9);        //敌军全体

	private final int value;

	TargetType(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
