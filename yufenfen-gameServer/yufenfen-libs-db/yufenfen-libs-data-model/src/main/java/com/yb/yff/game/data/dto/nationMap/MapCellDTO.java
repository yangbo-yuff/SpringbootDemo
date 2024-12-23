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
 * @Class: MapCellDTO
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 地图单元格数据
 */
@Data
public class MapCellDTO extends MapCellBaseData {
	private String RNick;
	private Integer id;
	private Integer op_level;
	private Integer x;
	private Integer y;
	private String name;
	private Integer defender;
	private Integer cur_durable;
	private Integer max_durable;
	private Long occupy_time;
	private Long end_time;
	private Long giveUp_time;
}