package com.yb.yff.sb.data.dto.army;

import com.yb.yff.sb.data.dto.role.RoleData;
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
 * @Class: ArmyData
 * @CreatedOn 2024/10/28.
 * @Email: yangboyff@gmail.com
 * @Description: 军队数据
 */
@Data
public class ArmyData extends RoleData {
	private List<ArmyDTO> armyList;
}
