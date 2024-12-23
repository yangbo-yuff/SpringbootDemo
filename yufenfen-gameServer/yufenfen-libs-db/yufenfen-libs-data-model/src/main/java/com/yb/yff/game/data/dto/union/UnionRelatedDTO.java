package com.yb.yff.game.data.dto.union;

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
 * @Class: UnionLogDTO
 * @CreatedOn 2024/12/13.
 * @Email: yangboyff@gmail.com
 * @Description:  联盟相关数据
 */
@Data
public class UnionRelatedDTO {
	private UnionDTO union;
	private UnionApplyDTO apply;
	private UnionLogDTO log;
}
