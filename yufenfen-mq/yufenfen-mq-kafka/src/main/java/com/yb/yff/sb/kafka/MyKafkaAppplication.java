package com.yb.yff.sb.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

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
 * @Class: MyKafkaAppplication
 * @CreatedOn 2024/7/2.
 * @Email: yangboyff@gmail.com
 * @Description: 启动类
 */

@SpringBootApplication
@EnableKafka
public class MyKafkaAppplication {
    public static void main(String[] args) {
        SpringApplication.run(MyKafkaAppplication.class, args);
    }
}
