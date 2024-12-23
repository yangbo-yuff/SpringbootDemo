package com.yb.yff.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling  // 启用定时任务
@EnableAsync(proxyTargetClass=true)
@Slf4j
@ComponentScan(basePackages = {"com.yb.yff.game", "com.yb.yff.sb", "com.yb.yff.flux"})
public class SLGServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SLGServerApplication.class, args);
		log.info("逻辑服务启动成功");
	}
}
