package com.yb.yff.sb.data.dto.city;

import com.yb.yff.sb.data.dto.role.RoleData;
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
 * @Class: CityData
 * @CreatedOn 2024/10/27.
 * @Email: yangboyff@gmail.com
 * @Description: 主城数据体
 */
@Data
public class CityData extends RoleData {

	/**
	 * 主城
	 */
	private List<CityDTO> cityList;

}
