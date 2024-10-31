package com.yb.yff.sb.data.dto.interior;

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
 * @Class: CollectDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：内务
 */
@Data
public class OpenCollectResDTO extends GameBusinessResBaseDTO {
	private Integer cur_times;
	private Integer limit;
	private Integer next_time;
}
