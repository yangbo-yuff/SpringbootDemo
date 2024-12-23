package com.yb.yff.game.data.dto.role;

import lombok.Data;

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
 * @Class: RoleResDTO
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description:  游戏角色资源DTO
 */

@Data
public class RoleResourceData {
	/**
	 * 角色ID
	 */
	private Integer rid;
	/**
	 * 仓库容量
	 */
	private Integer depot_capacity;

	/**
	 * 令牌
	 */
	private Integer decree;

	/**
	 * 木
	 */
	private Integer wood;
	/**
	 * 木 产量
	 */
	private Integer wood_yield;

	/**
	 * 铁
	 */
	private Integer iron;
	/**
	 * 铁 产量
	 */
	private Integer iron_yield;

	/**
	 * 石头
	 */
	private Integer stone;
	/**
	 * 石头 产量
	 */
	private Integer stone_yield;

	/**
	 * 粮食
	 */
	private Integer grain;
	/**
	 * 粮食 产量
	 */
	private Integer grain_yield;

	/**
	 * 金币
	 */
	private Integer gold;
	/**
	 * 金币 产量
	 */
	private Integer gold_yield;
}
