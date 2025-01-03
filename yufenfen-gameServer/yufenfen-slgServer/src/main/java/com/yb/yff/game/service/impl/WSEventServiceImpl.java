package com.yb.yff.game.service.impl;

import com.yb.yff.game.data.dto.ClientSessionIdUidRidDTO;
import com.yb.yff.game.data.dto.role.EnterServerResDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.service.DelayedTask.IDelayedTaskListener;
import com.yb.yff.game.service.DelayedTask.IDelayedTaskService;
import com.yb.yff.game.service.IPushSessionManager;
import com.yb.yff.game.service.IWSEventService;
import com.yb.yff.game.service.business.IBusinessService;
import com.yb.yff.game.utils.TaskUitls;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import com.yb.yff.sb.flux.server.service.IWSMessageListener;
import com.yb.yff.sb.flux.server.service.IWSServerManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
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
 * @Class: RouterServiceImpl
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 业务路由服务
 */
@Service
@Slf4j
public class WSEventServiceImpl implements IWSEventService, IWSMessageListener, IDelayedTaskListener {

	@Autowired
	IWSServerManager wsServerManager;

	@Autowired
	Map<String, IBusinessService> businessServiceList;

	@Autowired
	IDelayedTaskService delayedTaskService;

	@Autowired
	IPushSessionManager pushSessionManager;

	/**
	 * 缓存客户端SessionID 和 对应的 uid rid
	 * key： sessionID
	 * value： uid_rid
	 */
	private Map<String, ClientSessionIdUidRidDTO> clientSessionId_uid_rid = new HashMap<>();

	/**
	 * 用户ID key
	 */
	public static final String KEY_USER_ID = "uid";

	/**
	 * 角色ID key
	 */
	public static final String KEY_ROLE_ID = "rid";

	public static final String TAG = "=========== SLGServer";


	/**
	 * 正在处理的任务
	 */
	private List<String> tasksInProgress = new ArrayList<>();

	@PostConstruct
	private void init() {
		// 添加事件监听器
		wsServerManager.addWSMessageListener(this);
	}

	/**
	 * 由实现者决定行为
	 * 网关:  转发业务到其它服务器
	 * 非网关:  执行业务逻辑
	 *
	 * @param session
	 * @param requestDTO
	 */
	@Override
	public void onMessage(WebSocketSession session, GameMessageEnhancedReqDTO requestDTO) {
		String requestName = requestDTO.getName();
		log.info(TAG + " onMessage requestName : " + requestName);

		String[] names = requestName.split("\\.");

		if (names.length != 2) {
			log.info(TAG + " onMessage 非法 requestName : " + requestName);
			return;
		}

		// 插入 uid rid
		ClientSessionIdUidRidDTO csUR = clientSessionId_uid_rid.get(requestDTO.getSessionClient2Gate());
		if (csUR != null) {
			requestDTO.setUid(csUR.getUid());
			requestDTO.setRid(csUR.getRid());
		}


		IBusinessService businessService = getBusinessService(names[0]);
		if (businessService == null) {
			log.info(TAG + " onMessage 业务不存在 : " + requestName);
			return;
		}

		// 检查 重复任务
		if (!checkTask(requestDTO)) {
			return;
		}

		businessLogic(session.getId(), requestDTO, businessService, names[1])
				.doOnNext(response -> {
					sendMessage(session.getId(), response);
					CacheUserRole2Session(session, response);
				})
				.then(Mono.fromRunnable(() -> {
					onFinishTask(requestDTO);
				}))
				.subscribe();
	}

	private Mono<GameMessageEnhancedResDTO> businessLogic(String sessionId, GameMessageEnhancedReqDTO requestDTO, IBusinessService businessService, String businessName) {
		return Mono.fromCallable(() -> businessService.dispathBusiness(businessName, requestDTO))
				.flatMap(resDTO -> {
					if (resDTO.getDelayedTasks() == null) {
						return Mono.just(resDTO);
					} else {
						resDTO.getDelayedTasks().forEach(delayedTask -> {
							delayedTask.setSessionId(sessionId);
							// 添加耗时任务
							delayedTaskService.addDelayedTask(delayedTask, this);
						});

						resDTO.setDelayedTasks(null);

						// 返回响应
						return Mono.just(resDTO);

					}
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/**
	 * 缓存角色信息 到  session
	 *
	 * @param session
	 * @param resDTO
	 */
	private void CacheUserRole2Session(WebSocketSession session, GameMessageEnhancedResDTO resDTO) {
		if (!resDTO.getName().equals("role.enterServer")) {
			return;
		}

		EnterServerResDTO enterServerResDTO = (EnterServerResDTO) resDTO.getMsg();
		RoleDTO role = enterServerResDTO.getRole();
		if (role == null) {
			return;
		}

		log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx CacheUserRole2Session sessionId = {}, uid = {}, rid = {}",
				session.getId(), role.getUid(), role.getRid());

		ClientSessionIdUidRidDTO csUR = new ClientSessionIdUidRidDTO(resDTO.getSessionClient2Gate(),role.getUid(), role.getRid());
		// 移除失效的玩家信息
		for (Map.Entry<String, ClientSessionIdUidRidDTO> entry : clientSessionId_uid_rid.entrySet()){
			ClientSessionIdUidRidDTO csiURB = entry.getValue();
			if (csiURB.getUid().equals(role.getUid()) && csiURB.getRid().equals(role.getRid())){
				clientSessionId_uid_rid.remove(entry.getKey());
				break;
			}
		}

		clientSessionId_uid_rid.put(resDTO.getSessionClient2Gate(), csUR);

		// 推送服务缓存 SessionID
		pushSessionManager.addSession(role.getRid(), session.getId(), resDTO.getSessionClient2Gate());
	}

	private IBusinessService getBusinessService(String typeName) {
		return businessServiceList.get(typeName);
	}

	/**
	 * 业务路由处理, 业务服务器处理结果返回给业务发起端（客户端）
	 *
	 * @param gameMessageEnhancedResDTO
	 */
	@Override
	public void sendMessage(String sessionID, GameMessageEnhancedResDTO gameMessageEnhancedResDTO) {
		log.info("===================== return to client Message " + gameMessageEnhancedResDTO.getName());
		wsServerManager.sendMessage(sessionID, gameMessageEnhancedResDTO);
	}

	/**
	 * 延时任务执行完成
	 *
	 * @param sessionID
	 * @param takskId
	 * @param result
	 */
	@Override
	public void onDelayedTaskFinish(String sessionID, String takskId, GameMessageEnhancedResDTO result) {
		sendMessage(sessionID, result);
		onFinishTask(takskId);
	}

	/**
	 * 任务完成
	 *
	 * @param requestDTO
	 */
	private void onFinishTask(GameMessageEnhancedReqDTO requestDTO) {
		String taskKey = TaskUitls.createTaskKey(requestDTO);
		onFinishTask(taskKey);
	}

	/**
	 * 任务完成
	 *
	 * @param taskKey
	 */
	private void onFinishTask(String taskKey) {
		tasksInProgress.remove(taskKey);
	}

	/**
	 * 检查任务是否在处理中
	 *
	 * @param requestDTO
	 * @return
	 */
	private boolean checkTask(GameMessageEnhancedReqDTO requestDTO) {
		String taskKey = TaskUitls.createTaskKey(requestDTO);

		return checkTask(taskKey);
	}

	/**
	 * 检查任务是否在处理中
	 *
	 * @param taskKey
	 * @return
	 */
	private synchronized boolean checkTask(String taskKey) {
		if (tasksInProgress.contains(taskKey)) {
			return false;
		}

		tasksInProgress.add(taskKey);
		return true;
	}
}
