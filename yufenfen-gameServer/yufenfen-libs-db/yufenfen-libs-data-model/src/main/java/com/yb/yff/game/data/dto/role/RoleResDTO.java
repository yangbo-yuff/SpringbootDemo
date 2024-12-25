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
 * @Class: GameRoleInfoDTO
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏角色
 */
@Data
public class RoleResDTO extends GameBusinessResBaseDTO {
	/**
	 * roleId
	 */
	private Integer rid;

	/**
	 * 用户UID
	 */
	private Integer uid;

	/**
	 * 头像Id
	 */
	private Integer headId;

	/**
	 * 性别，0:女 1男
	 */
	private Integer sex;

	private String nickName;

	/**
	 * 余额
	 */
	private Integer balance;

	/**
	 * 个人简介
	 */
	private String profile;
}
