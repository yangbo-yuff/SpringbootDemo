package com.yb.yff.game.data.dto.facility.config;

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
 * @Class: FacilitiesData
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 设备数据
 */
@Data
public class FacilitiesDTO {
    private List<FacilityDTO> facilities;
    /**
     * key: FacilityDTO.type
     */
    private Map<Integer, FacilityDTO> facilityMap;
}
