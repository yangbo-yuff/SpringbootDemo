package com.yb.yff.sb.kafka.controller;

import com.yb.yff.sb.kafka.data.MyKafkaListenerData;
import com.yb.yff.sb.kafka.data.TopicData;
import com.yb.yff.sb.kafka.service.KafkaProducerService;
import com.yb.yff.sb.kafka.service.MyKafkaconsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * @Class: KafakaController
 * @CreatedOn 2024/7/2.
 * @Email: yangboyff@gmail.com
 * @Description: kafkaDemo控制层
 */

@RestController
@RequestMapping("/kafka")
@Tag(name = "RPC 服务 - WebSocket 发送器的")
public class KafakaController {
    @Autowired
    MyKafkaconsumerService kafkaconsumerService;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostMapping("/sendMsg")
    @Operation(summary = "向话题 #{topic.getTopic()} 发送消息")
    public String sendMsg2Topic(@Valid  @RequestBody TopicData topic){
        kafkaProducerService.sendMessage(topic.getTopic(), topic.getMsg());
        return "Listener Register";
    }

    @PostMapping("/addListener")
    @Operation(summary = "添加对话题 #{myKafkaListenerData.getTopic()} 的监听")
    public String addListener(@Valid  @RequestBody MyKafkaListenerData myKafkaListenerData) {
        kafkaconsumerService.addKafkaListener(myKafkaListenerData.getTopic(), myKafkaListenerData.getGroupId());
        return "Listener added to topic: " + myKafkaListenerData.getTopic() + ", groupID: " + myKafkaListenerData.getGroupId();
    }

    @PostMapping("/addListenerWhenErrorRetry")
    @Operation(summary = "添加对话题 #{myKafkaListenerData.getTopic()} 的监听")
    public String addListenerWhenErrorRetry(@Valid  @RequestBody MyKafkaListenerData myKafkaListenerData) {
        kafkaconsumerService.addKafkaListenerRetry(myKafkaListenerData.getTopic(), myKafkaListenerData.getGroupId());
        return "Listener added to topic: " + myKafkaListenerData.getTopic() + ", groupID: " + myKafkaListenerData.getGroupId();
    }

    @PostMapping("/removeListener")
    @Operation(summary = "取消话题 #{myKafkaListenerData.getTopic()} 的订阅")
    public String removeListener(@Valid  @RequestBody MyKafkaListenerData myKafkaListenerData) {
        kafkaconsumerService.removeMesgremoveListener(myKafkaListenerData.getTopic(), myKafkaListenerData.getGroupId());

        return "Listener removed from topic " + myKafkaListenerData.getTopic() + ", groupID: " + myKafkaListenerData.getGroupId();
    }
}
