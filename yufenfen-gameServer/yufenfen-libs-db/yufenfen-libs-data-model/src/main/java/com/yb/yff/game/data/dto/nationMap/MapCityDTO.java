package com.yb.yff.game.data.dto.nationMap;

import lombok.Data;

import java.util.Date;

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
 * @Class: MapBuildDTO
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 地图建筑数据
 */
@Data
public class MapCityDTO extends MapCellBaseData {
	private Integer cityId;
	private Integer cur_durable;
	/**
	 * 是否是主要城池，0：不是，1：是
	 */
	private Boolean is_main;
	private Integer max_durable;
	private String name;
	private Date occupy_time;
	private Integer parent_id;
	private Integer union_id;
	private String union_name;
	private Integer x;
	private Integer y;
}
