package com.yb.yff.game.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
 * @Class: User
 * @CreatedOn 2024/10/23.
 * @Email: yangboyff@gmail.com
 * @Description: 链接测试
 */
@Data
@Document(collection = "users")
public class MongoUser {
	@Id
	private String id;
	private String name;
	private int age;
}
