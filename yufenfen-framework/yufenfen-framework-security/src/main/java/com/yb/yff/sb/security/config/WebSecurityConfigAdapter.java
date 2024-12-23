package com.yb.yff.sb.security.config;

import com.yb.yff.sb.security.Filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
 * @Class: WebSecurityConfigAdapter
 * @CreatedOn 2024/10/9.
 * @Email: yangboyff@gmail.com
 * @Description: web安全配置适配器
 */

@Slf4j
@EnableWebSecurity
@Configuration
public class WebSecurityConfigAdapter {
	@Resource
	private JwtAuthenticationFilter jwtAuthenticationFilter;


	/**
	 * 权限不够处理器 Bean
	 */
	@Resource
	private AccessDeniedHandler accessDeniedHandler;

	/**
	 * 认证失败处理类 Bean
	 */
	@Resource
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // 允许所有来源
		configuration.addAllowedMethod("*"); // 允许所有HTTP方法
		configuration.addAllowedHeader("*"); // 允许所有头部
//		configuration.setAllowCredentials(true); // 允许携带凭证（如Cookie）

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 开启跨域
				.cors(Customizer.withDefaults())
				// CSRF 禁用，因为不使用 Session
				.csrf(AbstractHttpConfigurer::disable)
				// 基于 token 机制，所以不需要 Session
				.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/public/**", "/account/login", "/account/logout", "/account/reLogin", "/account/register").permitAll()  // 不需要认证的接口
						.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  // 需要管理员权限的接口
						.anyRequest().authenticated()  // 其他所有请求都需要认证
				)
				.exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler));

		// 添加 JWT 过滤器
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		log.info("============ Finish Configuration Spring Security!!!");

		return http.build();
	}
}
