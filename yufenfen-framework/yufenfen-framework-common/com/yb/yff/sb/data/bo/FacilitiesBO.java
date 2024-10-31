package com.yb.yff.sb.data.bo;

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
 * @Class: FacilitiesData
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 设备数据
 */
@Data
public class FacilitiesBO {
    private List<FacilityBO> facilities;
}
