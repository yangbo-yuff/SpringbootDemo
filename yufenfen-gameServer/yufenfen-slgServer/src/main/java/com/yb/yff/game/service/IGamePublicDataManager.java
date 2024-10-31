package com.yb.yff.game.service;

import com.yb.yff.sb.data.dto.city.PositionDTO;

import java.util.List;

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
 * @Class: IgameConmentDataManager
 * @CreatedOn 2024/10/28.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏公共数据
 */
public interface IGamePublicDataManager {
	/**
	 * 根据中心坐标获取角色列表
	 * @param position
	 */
	List<Integer> getRolesByPosition(PositionDTO position);

	/**
	 * 更新中心坐标内的角色
	 * @return
	 */
	boolean updatePositionRole(PositionDTO position, List<Integer> roles);
}
