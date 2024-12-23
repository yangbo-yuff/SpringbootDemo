package com.yb.yff.game.data.constant.myEnum;

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
 * @Description: 建筑物类型
 */
public enum BuildType {
	/**
	 * 系统要塞
	 */
	MapBuildSysFortress(50),

	/**
	 * 系统城市
	 */
	MapBuildSysCity(51),

	/**
	 * 玩家要塞
	 */
	MapBuildFortress(56);

	private final Integer value;

	BuildType(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
}
