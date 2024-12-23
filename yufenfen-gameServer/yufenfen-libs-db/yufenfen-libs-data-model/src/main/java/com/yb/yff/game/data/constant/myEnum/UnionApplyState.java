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
 * @Description: 军队状态
 */
public enum UnionApplyState implements EnumUtils<Integer> {
	// 未处理
	UnionUntreated(0),

	// 拒绝
	UnionRefuse(1),

	// 通过
	UnionAdopt(1);

	private final int value;

	UnionApplyState(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
