package com.yb.yff.sb.kafka.service;

import com.yb.yff.sb.kafka.config.CustomerMsgErrorHandler;
import com.yb.yff.sb.kafka.config.CustomerMsgHandler;
import com.yb.yff.sb.kafka.config.DeadLetterHandler;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.List;
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
 * @Class: MyKafkaListenerConfig
 * @CreatedOn 2024/7/3.
 * @Email: yangboyff@gmail.com
 * @Description: KafkaListener配置类
 */

@Service
public class MyKafkaconsumerService {

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enable_auto_commit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private int auto_commit_interval;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String auto_offset_reset;

    @Value("#{'${spring.kafka.bootstrap-servers}'.split(',')}")
    private List<String> bootstrapServers;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 监听器列表
     */
    private HashMap<String, MessageListenerContainer> messageListeners = new HashMap<String, MessageListenerContainer>();

//    @Autowired
//    private KafkaListenerEndpointRegistry endpointRegistry;

    public void addKafkaListener(String topic, String groupId) {
        String key = topic + groupId;
        if (messageListeners.containsKey(key)) {
            var container = messageListeners.get(key);
            if(!checkStatus(container)){
                container.resume();
            }
            return;
        }

        // kafka 消费者
        DefaultKafkaConsumerFactory<String, String> factory = consumerFactory(groupId);

        ContainerProperties props = new ContainerProperties(topic);

        // 设置监听器
        props.setMessageListener(new CustomerMsgHandler());
        props.setGroupId(groupId);
        props.setAckMode(ContainerProperties.AckMode.MANUAL);

        ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(factory, props);
        // 启动
        container.start();

        messageListeners.put(key, container);
    }

    /***
     * 消费者工厂
     * @param groupId
     * @return
     */
    private DefaultKafkaConsumerFactory<String, String> consumerFactory(String groupId) {
        // consumer配置
        Map<String, Object> configMap = new HashMap();
        // 采用手动提交的方式
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enable_auto_commit);
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, auto_offset_reset);
        configMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, auto_commit_interval);
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configMap.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // 序列化
        Deserializer<String> stringDeserializer = new StringDeserializer();

        return new DefaultKafkaConsumerFactory<>(configMap, stringDeserializer, stringDeserializer);
    }


    /******* 失败后重试机制 ********/
    public void addKafkaListenerRetry(String topic, String groupId) {
        addKafkaListener(topic, groupId, false);
    }

    /**
     * 添加 Topeic
     *
     * @param topic
     * @param groupId
     * @param isDeadLetter
     */
    public void addKafkaListener(String topic, String groupId, boolean isDeadLetter) {
        String key = topic + groupId;
        if (messageListeners.containsKey(key)) {
            var container = messageListeners.get(key);
            if(!checkStatus(container)){
                container.resume();
            }
            return;
        }

        // kafka 消费者
        DefaultKafkaConsumerFactory<String, String> consumerFactory = consumerFactory(groupId);

        // 相关属性
        ContainerProperties props = new ContainerProperties(topic);

        // 设置监听器(区分死信队列与非死信队列)
        if (!isDeadLetter) {
            props.setMessageListener(new CustomerMsgErrorHandler());
        } else {
            props.setMessageListener(new DeadLetterHandler());
        }

        props.setGroupId(groupId);
        props.setAckMode(ContainerProperties.AckMode.MANUAL);

        ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, props);

        container.setCommonErrorHandler(errorHandler());

        // 非死信队列，则添加一个死信队列监听器
        if (!isDeadLetter) {
            addKafkaListener(topic + ".DLT", groupId, true);
        }

        container.start();

        messageListeners.put(key, container);
    }

    /***
     *
     * @return
     */
    public DefaultErrorHandler errorHandler() {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(2000);
        backOff.setMultiplier(2);
        backOff.setMaxInterval(10000);

        return new DefaultErrorHandler(recoverer, backOff);
    }

    /***
     *
     * @param topic
     * @param groupId
     */
    public void removeMesgremoveListener(String topic, String groupId) {
        String key = topic + groupId;
        if (!messageListeners.containsKey(key)) {
            return;
        }

        MessageListenerContainer messageListener = messageListeners.get(key);
        messageListener.stop();
        messageListeners.remove(key);
    }

    private boolean checkStatus(MessageListenerContainer container){
        return  (container.isRunning() && !container.isPauseRequested() && !container.isContainerPaused());
    }
}
