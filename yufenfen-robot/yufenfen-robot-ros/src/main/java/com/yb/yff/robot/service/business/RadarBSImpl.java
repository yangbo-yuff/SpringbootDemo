package com.yb.yff.robot.service.business;

import com.yb.yff.robot.data.constant.RobotPartsBusinessType;
import com.yb.yff.robot.data.constant.RobotPartsType;
import com.yb.yff.robot.service.ros.IPublisher;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

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
 * @Class: GameRoleServiceImpl
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description: 雷达业务服务
 */
@Service(RobotPartsBusinessType.ROBOT_PARTS_RADAR)
@Slf4j
public class RadarBSImpl extends BusinessServiceImpl {
	/**
	 * 雷达 零部件发送者
	 */
	@Autowired
	@Qualifier(RobotPartsType.ROBOT_PARTS_RADAR)
	IPublisher raderPublisher;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("list", this::doBusiness);
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doBusiness(GameMessageEnhancedReqDTO reqDTO) {
		return null;
	}
}
