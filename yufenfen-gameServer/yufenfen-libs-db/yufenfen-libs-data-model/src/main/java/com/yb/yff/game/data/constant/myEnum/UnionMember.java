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
 * @Description: 联盟成员
 */
public enum UnionMember implements EnumUtils<Integer> {
	// 盟主
	UnionChairman(0, "盟主"),

	// 副盟主
	UnionViceChairman(1, "副盟主"),

	// 普通成员
	UnionCommon(3, "普通成员");

	private final Integer value;
	private final String title;

	UnionMember(int value, String title) {
		this.value = value;
		this.title = title;
	}

	@Override
	public Integer getValue() {
		return value;
	}
	public String getTitle() {
		return title;
	}
}
