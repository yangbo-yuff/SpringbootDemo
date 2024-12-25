package com.yb.yff.game.data.dto.role;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
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
 * @Class: EnterServerDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：进入服务器
 */
@Data
public class EnterServerResDTO extends GameBusinessResBaseDTO {
	private RoleDTO role;
	private RoleResourceData roleRes;
	private Long time;
	private String token;
}
