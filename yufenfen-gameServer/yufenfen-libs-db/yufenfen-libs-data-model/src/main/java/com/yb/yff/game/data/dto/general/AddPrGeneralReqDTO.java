package com.yb.yff.game.data.dto.general;

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
 * @Class: AddPrGeneralReqDTO
 * @CreatedOn 2024/12/19.
 * @Email: yangboyff@gmail.com
 * @Description: 将领分配属性点
 */
@Data
public class AddPrGeneralReqDTO {
	private Integer compId;
	private Integer forceAdd;
	private Integer strategyAdd;
	private Integer defenseAdd;
	private Integer speedAdd;
	private Integer destroyAdd;
}
