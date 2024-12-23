package com.yb.yff.game.data.dto.nationMap.config;

import com.yb.yff.game.data.dto.nationMap.ConfigDTO;
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
 * @Class: nationMap
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 区域地图数据
 */
@Data
public class NationConfigDTO {

	private String title;

	private List<ConfigDTO> cfg;

	/**
	 * 一层key: ConfigDTO.type
	 * 二层key: ConfigDTO.level
	 */
	private Map<Integer, Map<Integer, ConfigDTO>> nmcMap;


	/**
	 * key: MBCustomConfigDTO.type
	 */
	private Map<Integer, MBCustomConfigDTO> nmccMap;
}
