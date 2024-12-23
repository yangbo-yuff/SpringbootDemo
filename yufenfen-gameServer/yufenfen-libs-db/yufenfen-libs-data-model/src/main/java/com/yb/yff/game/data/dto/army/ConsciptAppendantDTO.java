package com.yb.yff.game.data.dto.army;

import com.yb.yff.game.data.dto.role.RoleResourceData;
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
 * @Class: ConsciptAppendantDTO
 * @CreatedOn 2024/11/18.
 * @Email: yangboyff@gmail.com
 * @Description: 征兵附带信息
 */
@Data
public class ConsciptAppendantDTO {

	/**
	 * 军队
	 */
	private ArmyDTO army;

	/**
	 * 资源信息
	 */
	private RoleResourceData roleResource;
}
