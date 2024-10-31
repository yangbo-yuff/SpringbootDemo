package com.yb.yff.game.config;

import com.yb.yff.game.data.entity.MongoUser;
import com.yb.yff.game.service.MongoUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
 * @Class: MongoTest
 * @CreatedOn 2024/10/23.
 * @Email: yangboyff@gmail.com
 * @Description: mongo链接测试
 */
@Configuration
public class MongoLinkDemo {
	@Autowired
	MongoUserService mongoUserService;

	@PostConstruct
	public void test() {
		MongoUser user = new MongoUser();
		user.setName("yangbo");
		user.setAge(18);
		mongoUserService.createUser(user);
	}
}
