package com.yb.yff.robot.service.ros.impl;

import com.yb.yff.robot.data.constant.RobotPartsType;
import com.yb.yff.robot.service.ros.IPublisher;
import com.yb.yff.robot.service.ros.IRosListener;
import com.yb.yff.robot.service.ros.IRosListenerMgr;
import lombok.extern.slf4j.Slf4j;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.springframework.stereotype.Service;
import std_msgs.String;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: RadarNSImpl
 * @CreatedOn 2024/12/30.
 * @Email: yangboyff@gmail.com
 * @Description: ros 节点 服务
 */
@Service(RobotPartsType.ROBOT_PARTS_RADAR)
@Slf4j
public class RadarNSImpl extends AbstractNodeMain implements IPublisher, IRosListenerMgr {
	private ConnectedNode connectedNode;
	private Publisher<String> publisher;

	private Map<java.lang.String, Subscriber<String>> subscriberMap;

	private ConcurrentHashMap<java.lang.String, List<IRosListener>> topicListenerMap;

	@Override
	public void onStart(ConnectedNode connectedNode) {
		this.connectedNode = connectedNode;

		// 在 Spring Boot 启动时创建 ROS Publisher
		publisher = connectedNode.newPublisher("/hello_world_topic", String._TYPE);
	}

	@Override
	public void onShutdown(Node node) {
		// 在节点关闭时停止发布
		if (publisher != null) {
			publisher.shutdown();
		}
	}

	public void onShutdownComplete(Node node) {
	}

	public void onError(Node node, Throwable throwable) {
	}

	/**
	 * @return
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("hello_world_node");
	}

	/**
	 * 发送消息
	 *
	 * @param data
	 */
	@Override
	public void publish(java.lang.String data) {
		std_msgs.String message = publisher.newMessage();
		message.setData(data);
		publisher.publish(message);
	}


	/**
	 * 添加监听
	 *
	 * @param topic
	 * @param listener
	 */
	@Override
	public void addTopicListener(java.lang.String topic, IRosListener listener) {
		log.info("添加监听:{}", topic);
		// 创建一个 Subscriber 对象，订阅 "hello_world_topic"
		Subscriber<String> subscriber = connectedNode.newSubscriber(topic, String._TYPE);

		subscriberMap.put(topic, subscriber);
		topicListenerMap.computeIfAbsent(topic, k-> new ArrayList<>()).add(listener);

		subscriber.addMessageListener(message -> {
			log.info("Received message: {}", message.getData());
			getTopicListener(topic).forEach(topicListener-> topicListener.onMessage(message.getData()));
		});
	}

	/**
	 * 接收到消息
	 *
	 * @param topic
	 * @param listener
	 */
	@Override
	public void rmTopicListener(java.lang.String topic, IRosListener listener) {
		log.info("添加监听:{}", topic);
		if(!subscriberMap.containsKey(topic)){
			return;
		}

		for(IRosListener topicListener: getTopicListener(topic)){
			if(topicListener.equals(listener)){
				getTopicListener(topic).remove(topicListener);
				break;
			}
		}

		if(getTopicListener(topic) == null || getTopicListener(topic).size() == 0){
			return;
		}

		Subscriber<String> subscriber = subscriberMap.get(topic);

		subscriber.shutdown();

		subscriberMap.remove(topic);
	}

	private List<IRosListener> getTopicListener(java.lang.String topic){
		return topicListenerMap.computeIfAbsent(topic, k-> new ArrayList<>());
	}
}
