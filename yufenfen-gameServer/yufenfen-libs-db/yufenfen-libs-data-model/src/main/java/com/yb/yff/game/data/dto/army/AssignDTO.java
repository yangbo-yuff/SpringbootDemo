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
 * @Class: assign
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 派遣
 */
@Data
public class AssignDTO {
	private Integer armyId;
	private Integer cmd;
	private Integer x;
	private Integer y;
}
