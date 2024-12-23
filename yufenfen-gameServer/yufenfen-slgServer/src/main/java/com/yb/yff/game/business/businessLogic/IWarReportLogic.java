package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.sb.constant.ResponseCode;

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
 * @Class: IWarReportLogic
 * @CreatedOn 2024/12/18.
 * @Email: yangboyff@gmail.com
 * @Description: 战争逻辑
 */
public interface IWarReportLogic {
	/**
	 * 获取最近N次战斗报告
	 *
	 * @param rid
	 * @param lastNum
	 * @return
	 */
	LogicTaskResultDTO<List<WarReportDTO>> getLastWarReport(Integer rid, Integer lastNum);

	/**
	 * 阅读战斗报告
	 *
	 * @param rid
	 * @param reportId
	 * @return
	 */
	ResponseCode readWarReport(Integer rid, Integer reportId);
}
