package com.yb.yff.sb.data.dto.army;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
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
 * @Class: ArmyDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 军队DTO
 */
@Data
public class ArmyDTO extends GameBusinessResBaseDTO {
	private Integer cityId;
	private Integer cmd;
	private Integer[] con_cnts = new Integer[]{0, 0, 0};
	private Integer[] con_times = new Integer[]{0, 0, 0};
	private Integer end;
	private Integer from_x;
	private Integer from_y;
	private Integer[] generals = new Integer[]{0, 0, 0};
	private Integer id;
	private Integer order;
	private Integer[] soldiers = new Integer[]{0, 0, 0};
	private Integer start;
	private Integer state;
	private Integer to_x;
	private Integer to_y;
	private Integer union_id;
}
