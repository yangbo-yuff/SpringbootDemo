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
 * @Class: BuildType
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 建筑物操作类型
 */
public enum BuildingOperationType implements EnumUtils<Integer> {
	/**
	 * 无操作
	 */
	BUILD_NOTHING(0),
	/**
	 * 新建
	 */
	BUILD_NEW(1),

	/**
	 * 升级
	 */
	BUILD_UP(2),

	/**
	 * 拆除
	 */
	BUILD_DEL(3);

	private final Integer value;

	BuildingOperationType(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
