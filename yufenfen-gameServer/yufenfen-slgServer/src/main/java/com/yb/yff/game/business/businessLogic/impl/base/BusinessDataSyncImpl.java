package com.yb.yff.game.business.businessLogic.impl.base;

import com.yb.yff.game.business.businessLogic.ISyncExecute;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;
import lombok.extern.slf4j.Slf4j;

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
 * @Class: BusinessDataSyncImpl
 * @CreatedOn 2024/12/2.
 * @Email: yangboyff@gmail.com
 * @Description: 业务推送器
 */
@Slf4j
public abstract class BusinessDataSyncImpl<T> implements ISyncExecute<T> {
	IPushService pushService;


	String pushName = "business.push";

	/**
	 * 初始化推送前
	 *
	 * @param pushName
	 * @param pushService
	 */
	public void initBusinessPusher(String pushName, IPushService pushService) {
		this.pushName = pushName;
		this.pushService = pushService;
	}

	/**
	 * @param data
	 */
	public void pushData(Integer rid, T data) {
		if (pushService == null) {
			log.error("推送服务未传入, 请先调用【init】方法传入");
		}

		GameMessageEnhancedResDTO message = new GameMessageEnhancedResDTO();
		message.setName(pushName);
		message.setMsg(data);
		message.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		message.setSeq(0);

		pushService.sendMessage(rid, message);
	}
}
