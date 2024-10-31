package com.yb.yff.flux.server.config;

import com.yb.yff.flux.server.handler.impl.WSServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

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
 * @Class: MyWebSocketConfig
 * @CreatedOn 2024/10/4.
 * @Email: yangboyff@gmail.com
 * @Description: 配置 WebSocket 服务端
 */

@Configuration
@Slf4j
public class WSServerConfig {
	@Bean
	public HandlerMapping webSocketHandlerMapping( WSServerHandler webSocketHandler) {
		log.info("====== webSocketHandlerMapping");
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/ws", webSocketHandler);  // WebSocket will be accessible at ws://localhost:8080/ws

		SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
		handlerMapping.setUrlMap(map);
		handlerMapping.setOrder(1);

		return handlerMapping;
	}

	@Bean
	public WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
