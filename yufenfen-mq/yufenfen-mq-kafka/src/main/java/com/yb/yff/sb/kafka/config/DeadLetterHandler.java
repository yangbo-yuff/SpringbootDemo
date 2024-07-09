package com.yb.yff.sb.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

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
 * @Class: CustomerMsgHandler
 * @CreatedOn 2024/7/4.
 * @Email: yangboyff@gmail.com
 * @Description: 处理消息类
 */

@Slf4j
public class DeadLetterHandler implements AcknowledgingMessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        // doSomething
        System.out.println("------------ DLT onMessage topic " + data.topic() + ": " + data.value());

        // 因为前面设置了手动提交ack的方式，这里需要在消息处理完成后提交ack
        acknowledgment.acknowledge();
    }
}
