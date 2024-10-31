package com.yb.yff.sb.data.bo;

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
 * @Class: FacilityBO
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 设施业务数据
 */
@Data
public class FacilityPropertyBO {
	private String title;
	private String name;
	private Integer type;
	private String des;
	private List<Integer> additions;
	private List<FacilityPropertyAddition> addition_details;
	private List<FacilityPropertyCondition> conditions;
	private List<FacilityPropertyLevel> levels;
}

