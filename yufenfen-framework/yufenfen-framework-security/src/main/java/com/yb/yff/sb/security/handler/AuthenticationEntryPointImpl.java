package com.yb.yff.sb.security.handler;

import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.ResponseDTO;
import com.yb.yff.sb.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

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
 * @Class: AuthenticationEntryPointImpl
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: 尚未认证（登录）的情况下，访问一个需要认证的 URL 资源，返回 返回 {@link NetResponseCodeConstants#HTTP_UNAUTHORIZED} 错误码。，从而使前端重定向到登录页
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), authException);

		// 返回 用户未登录
		ServletUtils.writeJSON(response, ResponseDTO.error(NetResponseCodeConstants.HTTP_UNAUTHORIZED));
	}
}
