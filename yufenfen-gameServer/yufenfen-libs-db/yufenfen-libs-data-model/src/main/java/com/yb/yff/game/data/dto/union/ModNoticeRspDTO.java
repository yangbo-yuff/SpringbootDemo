package com.yb.yff.game.data.dto.union;

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
 * @Class: ModNoticeRspDTO
 * @CreatedOn 2024/12/15.
 * @Email: yangboyff@gmail.com
 * @Description: 修改公告
 */
@Data
public class ModNoticeRspDTO extends GameBusinessResBaseDTO {
	//联盟id
	private Integer id;
	private String text;
}
