package com.yb.yff.game.utils;

import org.apache.commons.codec.digest.DigestUtils;

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
 * @Class: AccountUtils
 * @CreatedOn 2024/10/8.
 * @Email: yangboyff@gmail.com
 * @Description: 账户相关工具类
 */
public class AccountUtils {

	/**
	 * 随机生成指定长度的字符串
	 * @return
	 */
	public static String createpPsscode(int size) {
		// 使用 UUID 生成随机字符串，并取其前 8 位
		return UUID.randomUUID().toString().replace("-", "").substring(0, size);
	}

	public static String encodeMD5(String code){
		// 使用 DigestUtils 生成 MD5 哈希
		return DigestUtils.md5Hex(code);
	}
}
