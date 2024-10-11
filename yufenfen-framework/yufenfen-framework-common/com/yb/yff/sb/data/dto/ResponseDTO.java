package com.yb.yff.sb.data.dto;

import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import lombok.Data;

import java.io.Serializable;

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
 * @Class: ResponseDTO
 * @CreatedOn 2024/10/7.
 * @Email: yangboyff@gmail.com
 * @Description: 请求通用响应数据体
 */

@Data
public class ResponseDTO<T> implements Serializable {

	/**
	 * 响应码
	 *
	 * @see ResponseCode#getCode()
	 */
	private Integer code;
	/**
	 * 返回数据
	 */
	private T data;
	/**
	 * 错误提示，用户可阅读
	 *
	 * @see ResponseCode#getMsg() ()
	 */
	private String msg;

	public ResponseDTO(ResponseCode responseCode, T data) {
		this.code = responseCode.getCode();
		this.msg = responseCode.getMsg();
		this.data = data;
	}

	public static <T> ResponseDTO<T> success(T data) {
		return new ResponseDTO<>(NetResponseCodeConstants.SUCCESS, data);
	}
	public static <T> ResponseDTO<T> error(ResponseCode responseCode) {
		return new ResponseDTO<>(responseCode, null);
	}
}
