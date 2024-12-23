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
 * @Class: LvSkillReqDTO
 * @CreatedOn 2024/12/19.
 * @Email: yangboyff@gmail.com
 * @Description: 技能升级
 */
@Data
public class LvSkillReqDTO {
	//武将id
	private Integer gId;
	//位置0-2
	private Integer pos;
}
