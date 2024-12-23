package com.yb.yff.game.data.dto.general;

import com.yb.yff.game.jsondb.data.dto.general.GeneralList;
import com.yb.yff.game.jsondb.data.dto.general.General_armsArms;
import com.yb.yff.game.jsondb.data.dto.general.General_basicLevels;
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
 * @Class: GeneralConfigDTO
 * @CreatedOn 2024/11/8.
 * @Email: yangboyff@gmail.com
 * @Description: 将领配置数据
 */
@Data
public class GeneralConfigDTO {
	/**
	 * 将领配置表: value:cfgId
	 */
	private List<Integer> generalList;


	/**
	 * 将领配置表: key:cfgId
	 */
	private Map<Integer, GeneralList> generalMap;

	/**
	 * 兵种配置表: key:兵种id
	 */
	private Map<Integer, General_armsArms> armMap;

	/**
	 * 将领基本配置表: key: level
	 */
	private Map<Integer, General_basicLevels> levelMap;
}
