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
 * @Class: GateServerApplication
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 网关服务启动类
 */

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.yb.yff.game", "com.yb.yff.sb"})
public class GateServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateServerApplication.class, args);
        log.info("网关服务启动成功");
    }
}
