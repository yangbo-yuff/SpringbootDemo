//package com.yb.yff.sb.security.service;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * Copyright (c) 2024 to 2045  YangBo.
// * All rights reserved.
// * <p>
// * This software is the confidential and proprietary information of
// * YangBo. You shall not disclose such Confidential Information and
// * shall use it only in accordance with the terms of the license
// * agreement you entered into with YangBo.
// *
// * @author : YangBo
// * @Project: SpringbootDemo
// * @Class: CustomUserDetailsService
// * @CreatedOn 2024/10/10.
// * @Email: yangboyff@gmail.com
// * @Description: 用户信息服务
// */
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// 根据用户名加载用户信息，这里可以从数据库中查询
//		// 例如：return new User(username, password, authorities);
//		return null;
//	}
//}