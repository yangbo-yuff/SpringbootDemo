package com.yb.yff.game.data.dto;

import jakarta.validation.constraints.NotEmpty;
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
 * @Class: GameMessage
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 登陆信息
 */

@Data
public class UserInfoDTO {
	@NotEmpty(message = "username不能为空")
	private String username;
	@NotEmpty(message = "password")
	private String password;
	private String hardware;
}