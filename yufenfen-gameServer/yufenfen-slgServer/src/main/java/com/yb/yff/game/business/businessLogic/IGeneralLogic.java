package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.general.*;
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
 * @Class: IGeneralLogic
 * @CreatedOn 2024/11/8.
 * @Email: yangboyff@gmail.com
 * @Description: 将领相关业务逻辑
 */
public interface IGeneralLogic {
	/**
	 * 获取当前角色所有将领
	 * @return
	 */
	List<GeneralDTO> getRoleGenerals(Integer rid);

	/**
	 * 抽卡
	 * @param rid
	 * @param drawTimes
	 * @return
	 */
	List<GeneralDTO> drawGeneral(Integer rid, Integer drawTimes);

	/**
	 * 准备流放
	 * @param rid
	 * @param convertIDs
	 * @return 执行流放的将领
	 */
	ResponseCode convertGeneral(Integer rid, List<Integer> convertIDs, ConvertResDTO convertRes);

	/**
	 * 合成
	 * @param rid
	 * @param composeGeneral
	 * @return
	 */
	LogicTaskResultDTO<List<GeneralDTO>> composeGeneral(Integer rid, ComposeGeneralDTO composeGeneral);

	/**
	 * 分配属性点
	 * @param rid
	 * @param addPrGeneralReq
	 * @return
	 */
	LogicTaskResultDTO<GeneralDTO> addPrGeneral(Integer rid, AddPrGeneralReqDTO addPrGeneralReq);

	/**
	 * 分配技能
	 * @param rid
	 * @param killReq
	 * @return
	 */
	ResponseCode upGeneralKill(Integer rid, UpDownSkillReqDTO killReq);

	/**
	 * 分配技能
	 * @param rid
	 * @param killReq
	 * @return
	 */
	ResponseCode downGeneralKill(Integer rid, UpDownSkillReqDTO killReq);

	/**
	 * 分配技能
	 * @param rid
	 * @param killReq
	 * @return
	 */
	ResponseCode lvGeneralKill(Integer rid, LvSkillReqDTO killReq);
}
