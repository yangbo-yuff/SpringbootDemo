package com.yb.yff.robot;

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
 * @Class: JavaRosApplication
 * @CreatedOn 2024/12/30.
 * @Email: yangboyff@gmail.com
 * @Description: ros
 */
@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.yb.yff.sb"})
public class JavaRosApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaRosApplication.class, args);
		log.info("ROS节点服务启动成功");
	}
}
