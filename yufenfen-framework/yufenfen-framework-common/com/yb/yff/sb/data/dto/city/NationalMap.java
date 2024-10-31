package com.yb.yff.sb.data.dto.city;

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
 * @Class: NationalMap
 * @CreatedOn 2024/10/27.
 * @Email: yangboyff@gmail.com
 * @Description: 区域地图数据
 */
@Data
public class NationalMap {
 private Integer id;
 private PositionDTO pos;
 private Integer type;
 private Integer level;
}
