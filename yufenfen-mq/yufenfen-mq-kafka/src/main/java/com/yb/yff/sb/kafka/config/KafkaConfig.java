//package com.yb.yff.sb.kafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
//import org.springframework.kafka.retrytopic.DltStrategy;
//import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
//import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
//import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Copyright (c) 2024 to 2045  YangBo.
// * All rights reserved.
// * <p>
// * This software is the confidential and proprietary information of
// * YangBo. You shall not disclose such Confidential Information and
// * shall use it only in accordance with the terms of the license
// * agreement you entered into with YangBo.
// *
// * @author : YangBo
// * @Project: SpringbootDemo
// * @Class: KafkaConfig
// * @CreatedOn 2024/7/7.
// * @Email: yangboyff@gmail.com
// * @Description: kafka 自定义配置
// */
//
//@Configuration
//public class KafkaConfig {
//    @Bean
//    public DefaultErrorHandler errorHandler(ConsumerFactory<String, String> consumerFactory, KafkaTemplate<String, String> kafkaTemplate) {
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
//                (consumerRecord, exception) -> new org.apache.kafka.common.TopicPartition(customDltTopic, consumerRecord.partition())) {
//            @Override
//            public void accept(org.apache.kafka.clients.consumer.ConsumerRecord<?, ?> record, Exception exception) {
//                // 自定义的回调逻辑
//                System.out.println("Handling dead letter: " + record.value() + " due to " + exception.getMessage());
//                super.accept(record, exception);
//            }
//        };
//
//
//
//        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
//        backOff.setInitialInterval(2000);
//        backOff.setMultiplier(2);
//        backOff.setMaxInterval(60000);
//
//        return new DefaultErrorHandler(recoverer, backOff);
//    }
//}
