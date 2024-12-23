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
 * @Class: ConfigResDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：国际地图配置
 */
@Data
public class ConfigDTO {
	private Integer type;
	private Integer level;
	private Integer defender;
	private Integer durable;
	private Integer grain;
	private Integer iron;
	private String name;
	private Integer stone;
	private Integer wood;
}