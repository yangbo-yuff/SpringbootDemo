package com.yb.yff.sb.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

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
 * @Class: JwtAuthenticationToken
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: 用于封装JWT认证后的信息
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final UserDetails principal;

	public JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		setAuthenticated(true);  // 设置为已认证
	}

	@Override
	public Object getCredentials() {
		return null;  // JWT 没有具体的凭证，因此返回 null
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}