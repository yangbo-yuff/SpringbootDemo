package com.yb.yff.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
 * @Class: ChatServerApplication
 * @CreatedOn 2024/10/13.
 * @Email: yangboyff@gmail.com
 * @Description: 聊天服务 启动类
 */
@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.yb.yff.game", "com.yb.yff.sb", "com.yb.yff.flux"})
public class ChatServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatServerApplication.class, args);
		log.info("聊天服务启动成功");
	}
}
