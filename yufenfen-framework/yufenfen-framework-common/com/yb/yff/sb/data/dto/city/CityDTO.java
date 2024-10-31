package com.yb.yff.sb.data.dto.city;

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
 * @Class: BuildDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：建筑
 */
@Data
public class CityDTO {
	private Integer cityId;
	private Integer cur_durable;
	private Integer is_main;
	private Integer level;
	private Integer max_durable;
	private String name;
	private Integer occupy_time;
	private Integer parent_id;
	private Integer rid;
	private Integer union_id;
	private String union_name;
	private Integer x;
	private Integer y;
}
