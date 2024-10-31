package com.yb.yff.game.service;

import com.yb.yff.game.data.entity.MongoUser;
import com.yb.yff.game.repository.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

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
 * @Class: MongoUserService
 * @CreatedOn 2024/10/23.
 * @Email: yangboyff@gmail.com
 * @Description: 链接测试
 */
@Service
public class MongoUserService {
	@Autowired
	private MongoUserRepository userRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public MongoUser createUser(MongoUser user) {
		return userRepository.save(user);
	}

	public MongoUser getUserByName(String name) {
		return userRepository.findByName(name);
	}

	public List<MongoUser> findUsersByCustomQuery(int age) {
		Query query = new Query();
		query.addCriteria(Criteria.where("age").gt(age));
		return mongoTemplate.find(query, MongoUser.class);
	}
}
