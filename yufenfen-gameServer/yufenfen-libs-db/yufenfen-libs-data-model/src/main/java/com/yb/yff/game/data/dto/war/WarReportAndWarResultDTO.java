package com.yb.yff.game.data.dto.war;

import com.yb.yff.game.data.bo.WarResultBO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
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
 * @Class: WarReportAndWarResultDTO
 * @CreatedOn 2024/12/4.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗报告与战斗结果
 */
@Data
public class WarReportAndWarResultDTO {
	private WarReportDTO warReport;
	private WarResultBO warResultBO;
}
