package com.yb.yff.game.data.dto.army;

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
 * @Class: Dispose
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 将领配置到军队DTO
 */
@Data
public class DisposeDTO extends MyOneDTO {
	private Integer generalId;
	/**
	 * -1: 退出军队, 其它：将领要加入的位置
	 */
	private Integer position;
}
