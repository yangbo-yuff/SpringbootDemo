package com.yb.yff.game.data.dto.city;

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
 * @Class: NationalMap
 * @CreatedOn 2024/10/27.
 * @Email: yangboyff@gmail.com
 * @Description: 区域地图数据
 */
@Data
public class NationalMaps {
	/**
	 * 区域地图数据 key: posKey = CityPositionUtils.position2Number(x, y);
	 */
	private Map<Integer, NationalMap> nationalMapMap;
	private List<NationalMap> sysBuildList;
}
