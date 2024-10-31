package com.yb.yff.sb.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

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
 * @Class: KafkaProducerService
 * @CreatedOn 2024/7/4.
 * @Email: yangboyff@gmail.com
 * @Description: kafka生产者服务
 */

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<Object, Object> template;


    public void sendMessage(String topic, String msg) {
        try {
            String defaultTopic = template.getDefaultTopic();
            CompletableFuture<SendResult<Object, Object>> result = template.send(topic, msg);
            System.out.println("------------ sendMessage result: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
