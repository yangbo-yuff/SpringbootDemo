package com.yb.yff.sb.data.dto.army;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.role.RoleResourceData;
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
 * @Class: conscriptDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 征兵
 */
@Data
public class ConscriptResDTO extends GameBusinessResBaseDTO {
	/**
	 * 军队
	 */
	private ArmyDTO army;

	/**
	 * 其它信息
	 */
	private RoleResourceData role_res;
}
