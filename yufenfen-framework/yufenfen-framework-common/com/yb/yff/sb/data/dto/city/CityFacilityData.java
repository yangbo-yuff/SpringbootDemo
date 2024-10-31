package com.yb.yff.sb.data.dto.city;

import com.yb.yff.sb.data.bo.FacilityBO;
import com.yb.yff.sb.data.dto.role.RoleData;
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
 * @Class: FacilitiesDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：主城设施
 */
@Data
public class CityFacilityData extends RoleData {
	/**
	 * 城市设施  <cityId, List<FacilitiesDTO>>
	 *     List<FacilitiesDTO>为按ID排序的设施列表
	 */
	private Map<Integer, List<FacilityBO>> facilities;
}
