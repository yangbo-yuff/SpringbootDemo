package com.yb.yff.sb.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * @Class: JwtUtil
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: JWT工具类
 */
@Component
public class JwtUtil {
	private static final String SECRET_KEY = "mysecretkey"; // 密钥

	// 生成 JWT Token
	public static String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	private static String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 小时过期
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	// 验证 JWT Token 是否有效
	public static Boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	// 从 Token 中提取用户名
	public static String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	// 检查 Token 是否过期
	private static Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private static Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	// 解析 Token 的 Claims
	private static Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
}
