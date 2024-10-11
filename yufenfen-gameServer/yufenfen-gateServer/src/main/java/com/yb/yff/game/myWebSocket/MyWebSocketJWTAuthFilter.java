package com.yb.yff.game.myWebSocket;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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
 * @Class: MyWebSocketAuthInterceptor
 * @CreatedOn 2024/10/5.
 * @Email: yangboyff@gmail.com
 * @Description: 用于拦截 WebSocket 握手，并在握手前进行身份验证
 */

@Component
public class MyWebSocketJWTAuthFilter implements WebFilter {
    private static final String SECRET_KEY = "your-secret-key"; // JWT 钥

    public boolean checkToken = false;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if(checkToken) {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            // 最基本验证
            if (token == null && !token.startsWith("Bearer ")) {
                // 没有 token
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // TODO check token
        }

        // 继续处理请求
        return chain.filter(exchange);
    }
}