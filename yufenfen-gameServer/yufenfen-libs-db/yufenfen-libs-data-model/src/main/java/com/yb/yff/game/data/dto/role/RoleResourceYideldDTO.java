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
public class RoleResourceYideldDTO {
	/**
	 * 角色ID
	 */
	private Integer rid;

	/**
	 * 木 产量
	 */
	private Integer wood_yield;

	/**
	 * 铁 产量
	 */
	private Integer iron_yield;

	/**
	 * 石头 产量
	 */
	private Integer stone_yield;

	/**
	 * 粮食 产量
	 */
	private Integer grain_yield;

	/**
	 * 金币 产量
	 */
	private Integer gold_yield;

	public RoleResourceYideldDTO(){

	}

	public RoleResourceYideldDTO(Integer rid){
		this.rid = rid;
		this.wood_yield = 0;
		this.iron_yield = 0;
		this.stone_yield = 0;
		this.grain_yield = 0;
		this.gold_yield = 0;
	}
}
