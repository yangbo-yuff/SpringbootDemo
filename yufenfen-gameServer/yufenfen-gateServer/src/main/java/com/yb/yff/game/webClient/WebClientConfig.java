package com.yb.yff.game.webClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

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
 * @Class: WebClientConfig
 * @CreatedOn 2024/10/13.
 * @Email: yangboyff@gmail.com
 * @Description: 配置websocket服务器器与Http服务器通信
 */

@Configuration
public class WebClientConfig {
	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
}
