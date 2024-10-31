package com.yb.yff.sb.data.dto.general;

import lombok.Data;

import java.util.List;

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
 * @Class: MyGenerals
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：武将
 */
@Data
public class GeneralDTO {
	private Integer attack_distance;
	private Integer cfgId;
	private Integer cityId;
	private Integer curArms;
	private Integer defense_added;
	private Integer destroy_added;
	private Integer exp;
	private Integer force_added;
	private Integer hasPrPoint;
	private Integer id;
	private Integer level;
	private Integer order;
	private Integer parentId;
	private Integer physical_power;
	private List<Object> skills;
	private Integer speed_added;
	private Integer star;
	private Integer star_lv;
	private Integer state;
	private Integer strategy_added;
	private Integer usePrPoint;
}
