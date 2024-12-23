package com.yb.yff.game.data.dto.nationMap;

import com.yb.yff.game.data.constant.myEnum.BuildingOperationType;
import lombok.Data;

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
public class MapBuildDTO extends MapCellDTO {
	private Integer wood;
	private Integer iron;
	private Integer stone;
	private Integer grain;

	/**
	 * 要塞操作类型 的操作类型，0 无操作，1 新建，2升级，3拆除
	 * 配合 endTime 使用
	 * 默认0
	 */
	private BuildingOperationType operationType;
}
