package com.yb.yff.sb.data.dto.city;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.role.RoleResourceData;
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
 * @Class: FacilitiesDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：设施升级
 */
@Data
public class UpFacilitieResDTO extends GameBusinessResBaseDTO {
	private Integer cityId;
	private FacilityDTO facilitie;

	private RoleResourceData role_res;
}
