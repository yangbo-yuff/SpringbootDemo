package com.yb.yff.game.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
 * @Class: MyBatisPlusConfig
 * @CreatedOn 2024/10/6.
 * @Email: yangboyff@gmail.com
 * @Description: MyBatis-Plus 配置类
 */

@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.yb.yff.game.mapper"})
public class MyBatisPlusConfig {
	// 如果有需要自定义的配置，可以在这里添加
}
