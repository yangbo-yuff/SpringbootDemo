package com.yb.yff.game.data.dto.war;

import lombok.Data;

import java.util.ArrayList;
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
 * @Class: WarRoundIsEndDTO
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description:
 */
@Data
public class WarRoundDTO {
	private List<HitDTO> b = new ArrayList<>();
}
