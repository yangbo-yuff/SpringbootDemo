package com.yb.yff.sb.security.handler;

import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.ResponseDTO;
import com.yb.yff.sb.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

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
 * @Class: AccessDeniedHandlerImpl
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: 在已经认证（登录）但是没有权限的情况下，访问一个需要认证的 URL 资源，返回 {@link NetResponseCodeConstants#HTTP_FORBIDDEN} 错误码。
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// 打印 warn 的原因是，不定期合并 warn，看看有没恶意破坏
		log.warn("[commence][访问 URL({}) 时，用户 权限不够]", request.getRequestURI(), accessDeniedException);
		// 返回 401
		ServletUtils.writeJSON(response, ResponseDTO.error(NetResponseCodeConstants.HTTP_FORBIDDEN));
	}
}
