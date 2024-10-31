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
 * @Class: GameRoleInfoDTO
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏角色
 */
@Data
public class CreateDTO {

	/**
	 * 用户UID
	 */
	private Integer uid;

	/**
	 * nick_name
	 */
	private String nickName;

	/**
	 * 性别，0:女 1男
	 */
	private Byte sex;

	/**
	 *
	 */
	private Integer sid;

	/**
	 * 头像Id
	 */
	private Integer headId;
}
