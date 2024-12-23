package com.yb.yff.game.data.dto;

import com.yb.yff.game.jsondb.data.dto.BasicConscript;
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
 * @Class: ConscriptCheckResultDTO
 * @CreatedOn 2024/11/19.
 * @Email: yangboyff@gmail.com
 * @Description: 征兵核查结果数据
 */
@Data
public class ConscriptCheckResultDTO {
	private List<BasicConscript> needResourceList;
	private BasicConscript needResourceTotal;
}
