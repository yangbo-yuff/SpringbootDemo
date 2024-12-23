package com.yb.yff.game.data.dto.city;

import com.yb.yff.game.data.dto.nationMap.MapCellDTO;
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
 * @Class: MapCellReqDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：建筑
 */
@Data
public class BuildDTO extends MapCellDTO {
	private Integer parent_id;
	private Integer union_id;
	private String union_name;
}
