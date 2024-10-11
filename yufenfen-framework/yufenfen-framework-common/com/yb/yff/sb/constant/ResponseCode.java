package com.yb.yff.sb.constant;

import lombok.Data;

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
 * @Class: ResponseCode
 * @CreatedOn 2024/10/7.
 * @Email: yangboyff@gmail.com
 * @Description: Http请求响应类型
 */

@Data
public class ResponseCode {

	/**
	 * 错误码
	 */
	private final Integer code;
	/**
	 * 错误提示
	 */
	private final String msg;

	public ResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
