package com.yb.yff.sb.utils;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

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
 * @Class: ServletUtils
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: 客户端的 和 服务器 的中间层工具
 */
public class ServletUtils {
	public static void writeJSON(HttpServletResponse response, Object object) {
		String content = JSON.toJSONString(object);
		JakartaServletUtil.write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
	}

}
