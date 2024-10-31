package com.yb.yff.sb.data.dto.city;

import com.yb.yff.sb.data.dto.role.RoleData;
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
 * @Class: CenterPosData
 * @CreatedOn 2024/10/28.
 * @Email: yangboyff@gmail.com
 * @Description: 角色中心位置
 */
@Data
public class CenterPosData extends RoleData {
	private PositionDTO centerPos;
}
