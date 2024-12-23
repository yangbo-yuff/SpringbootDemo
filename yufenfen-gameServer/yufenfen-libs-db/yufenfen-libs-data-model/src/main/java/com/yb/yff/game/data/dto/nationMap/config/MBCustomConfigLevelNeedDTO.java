package com.yb.yff.game.data.dto.nationMap.config;

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
 * @Class: MBCustomConfigLevelNeedDTO
 * @CreatedOn 2024/11/2.
 * @Email: yangboyff@gmail.com
 * @Description: 建筑级别数据
 */
@Data
public class MBCustomConfigLevelNeedDTO {
	private Integer decree;

	private Integer iron;

	private Integer wood;

	private Integer grain;

	private Integer stone;

	private Integer gold = 0;
}
