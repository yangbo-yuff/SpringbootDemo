package com.yb.yff.game.data.dto.facility.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
 * @Class: FacilityDTO
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 设施业务数据
 */
@Data
public class FacilityPropertyDTO {
	private String des;
	private List<Integer> additions;
	private String name;
	private String title;
	private Integer type;
	private List<FacilityPropertyConditionDTO> conditions;
	private List<FacilityPropertyLevel> levels;
	private List<FacilityPropertyAdditionDTO> additionDetails;
	/**
	 * Key: FacilityPropertyLevel.level
	 */
	private Map<Integer,FacilityPropertyLevel> leveMap;
}

