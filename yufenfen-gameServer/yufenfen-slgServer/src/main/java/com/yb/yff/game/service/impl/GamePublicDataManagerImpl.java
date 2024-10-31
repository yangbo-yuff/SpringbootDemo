package com.yb.yff.game.service.impl;

import com.yb.yff.game.service.IGamePublicDataManager;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.sb.data.dto.city.PositionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
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
 * @Class: GamePublicDataManagerImpl
 * @CreatedOn 2024/10/28.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏公用数据
 */
@Service
public class GamePublicDataManagerImpl implements IGamePublicDataManager {
	/**
	 * 以position为中心位置的角色列表
	 */
	private ConcurrentHashMap<Integer, List<Integer>> rolesCenterPosition = new ConcurrentHashMap<>();

	/**
	 * 根据中心坐标获取角色列表
	 *
	 * @param position
	 */
	@Override
	public List<Integer> getRolesByPosition(PositionDTO position) {
		Integer posNum = CityPositionUtils.position2Number(position);
		return rolesCenterPosition.get(posNum);
	}

	/**
	 * 更新中心坐标内的角色
	 *
	 * @param position
	 * @param roles
	 * @return
	 */
	@Override
	public boolean updatePositionRole(PositionDTO position, List<Integer> roles) {
		Integer posNum = CityPositionUtils.position2Number(position);

		rolesCenterPosition.put(posNum, roles);

		return true;
	}
}
