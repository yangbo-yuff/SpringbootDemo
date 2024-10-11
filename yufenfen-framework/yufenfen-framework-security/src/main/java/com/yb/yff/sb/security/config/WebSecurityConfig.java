package com.yb.yff.sb.security.config;

import com.yb.yff.sb.security.handler.AccessDeniedHandlerImpl;
import com.yb.yff.sb.security.handler.AuthenticationEntryPointImpl;
import com.yb.yff.sb.utils.JwtUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

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
 * @Class: WebSecurityConfig
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: Web 安全配置
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class WebSecurityConfig {
	/**
	 * 认证失败处理类 Bean
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPointImpl();
	}


	/**
	 * 权限不够处理器 Bean
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandlerImpl();
	}
}
