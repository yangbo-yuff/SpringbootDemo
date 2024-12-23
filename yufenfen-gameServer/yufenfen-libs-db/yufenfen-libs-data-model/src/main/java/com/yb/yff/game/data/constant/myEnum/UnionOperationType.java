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
 * @Description: 联盟操作类型
 */
public enum UnionOperationType implements EnumUtils<Integer> {
	//创建
	UnionOpCreate(0),
	//解散
	UnionOpDismiss(1),
	 //加入
	UnionOpJoin(2),
	//退出
	UnionOpExit(3),
	//踢出
	UnionOpKick(4),
	//任命
	UnionOpAppoint(5),
	//禅让
	UnionOpAbdicate(6),
	//修改公告
	UnionOpModNotice(7);

	private final Integer value;

	UnionOperationType(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
