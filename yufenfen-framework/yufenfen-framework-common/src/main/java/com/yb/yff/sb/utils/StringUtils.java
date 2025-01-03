package com.yb.yff.sb.utils;

import java.util.UUID;

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
 * @Class: StringUtils
 * @CreatedOn 2024/10/18.
 * @Email: yangboyff@gmail.com
 * @Description: 字符串处理工具
 */
public class StringUtils {

	/**
	 * 随机生成指定长度的字符串
	 *
	 * @return
	 */
	public static String createStrBySize(int size) {
		// 使用 UUID 生成随机长度为 size 的字符串，
		return UUID.randomUUID().toString().replace("-", "").substring(0, size);
	}
}
