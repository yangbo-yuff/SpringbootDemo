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
	private Integer woodYield;

	/**
	 * 铁 产量
	 */
	private Integer ironYield;

	/**
	 * 石头 产量
	 */
	private Integer stoneYield;

	/**
	 * 粮食 产量
	 */
	private Integer grainYield;

	/**
	 * 金币 产量
	 */
	private Integer goldYield;

	public RoleResourceYideldDTO(){

	}

	public RoleResourceYideldDTO(Integer rid){
		this.rid = rid;
		this.woodYield = 0;
		this.ironYield = 0;
		this.stoneYield = 0;
		this.grainYield = 0;
		this.goldYield = 0;
	}
}
