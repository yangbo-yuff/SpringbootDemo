package com.yb.yff.game.data.dto.city;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
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
 * @Class: FacilitiesDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：设施
 */
@Data
public class FacilityResDTO extends GameBusinessResBaseDTO {
	private Integer cityId;
	private List<FacilityDTO> facilities;
}
