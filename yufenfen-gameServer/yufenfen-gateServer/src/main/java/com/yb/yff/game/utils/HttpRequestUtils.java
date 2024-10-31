package com.yb.yff.game.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
 * @Class: HttpRequestUtils
 * @CreatedOn 2024/10/13.
 * @Email: yangboyff@gmail.com
 * @Description: http请求相关工具
 */
public class HttpRequestUtils {
	/**
	 * 将json字符串转换为uri字符串
	 * @param jsonStr
	 * @return
	 */
	public static URI jsonStr2Uri(String businessID, String jsonStr) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(businessID);

		JSONObject jsonObject =JSON.parseObject(jsonStr);
		jsonObject.entrySet().forEach(entry -> {
			uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
		});

		return uriComponentsBuilder.build().toUri();
	}
}
