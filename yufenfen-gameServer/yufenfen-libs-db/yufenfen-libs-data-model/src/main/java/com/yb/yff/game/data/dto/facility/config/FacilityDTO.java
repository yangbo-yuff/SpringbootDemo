package com.yb.yff.game.data.dto.facility.config;

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
 * @Class: FacilityDTO
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 设备业务数据
 */
@Data
public class FacilityDTO {
 private Integer level;
 private String name;
 private Integer type;
 private Integer upTime;
 private FacilityPropertyDTO property;
}
