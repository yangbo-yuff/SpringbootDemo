package com.yb.yff.sb.data.dto.role;

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
 * @Class: PosTagDTO
 * @CreatedOn 2024/10/28.
 * @Email: yangboyff@gmail.com
 * @Description: 打标签位置
 */
@Data
public class PosTagTypeDTO {
	private Integer type;
	private Integer x;
	private Integer y;
	private String name;
}
