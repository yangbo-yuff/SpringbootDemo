package com.yb.yff.game.data.constant;

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
 * @Class: EnumUtils
 * @CreatedOn 2024/11/27.
 * @Email: yangboyff@gmail.com
 * @Description: 枚举工具类
 */
public interface EnumUtils<T> {
	T getValue();

	static <T, E extends Enum<E> & EnumUtils<T>> E fromValue(Class<E> enumClass, T value) {
		for (E e : enumClass.getEnumConstants()) {
			if (e.getValue().equals(value)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Invalid value: " + value);
	}
}
