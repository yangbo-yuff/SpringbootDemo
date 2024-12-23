package com.yb.yff.game.data.dto.nationMap;

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
 * @Class: RoleData
 * @CreatedOn 2024/10/26.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏角色数据体
 */
@Data
public class MapCellBaseData {
	/**
	 * 归属玩家，0/null 为无
	 */
	private Integer rid;

	/**
	 * 类型
	 */
	private Integer type;


	/**
	 * 级别
	 */
	private Integer level;
}
