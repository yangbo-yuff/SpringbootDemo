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
 * @Class: VerifyReqDTO
 * @CreatedOn 2024/12/14.
 * @Email: yangboyff@gmail.com
 * @Description: 审核申请
 */
@Data
public class VerifyReqDTO {
	//操作的记录id
	private Integer id;
	//1是拒绝，2是通过
	private Integer decide;
}
