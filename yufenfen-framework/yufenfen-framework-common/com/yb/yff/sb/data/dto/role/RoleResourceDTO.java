package com.yb.yff.sb.data.dto.role;

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
public class RoleResourceDTO {
	/**
	 * roleId
	 */
	private Integer rid;

	/**
	 * 木
	 */
	private Integer wood;

	/**
	 * 铁
	 */
	private Integer iron;

	/**
	 * 石头
	 */
	private Integer stone;

	/**
	 * 粮食
	 */
	private Integer grain;

	/**
	 * 金币
	 */
	private Integer gold;

	/**
	 * 令牌
	 */
	private Integer decree;
}
