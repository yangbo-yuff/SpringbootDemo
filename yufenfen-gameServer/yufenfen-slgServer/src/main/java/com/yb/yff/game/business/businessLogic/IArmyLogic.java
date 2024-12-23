package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.AssignDTO;
import com.yb.yff.game.data.dto.army.ConscriptDTO;
import com.yb.yff.game.data.dto.army.DisposeDTO;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;

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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 地图军队业务逻辑
 */

public interface IArmyLogic {

	/**
	 * 获取城市军队
	 *
	 * @param cityId
	 * @return
	 */
	List<WarArmyDTO> getCityArmy(Integer cityId);

	/**
	 * 获取指定军队
	 *
	 * @param cityId
	 * @param order
	 * @return
	 */
	WarArmyDTO getOrderArmy(Integer rid, Integer cityId, Integer order);

	/**
	 * 配置将领
	 *
	 * @param disposeDTO
	 * @return
	 */
	LogicTaskResultDTO<WarArmyDTO> configureGenerals(Integer rid, DisposeDTO disposeDTO);

	/**
	 * 配置士兵
	 *
	 * @param conscriptDTO
	 * @return
	 */
	LogicTaskResultDTO<List<TimeConsumingTask>> configureSoldiers(GameMessageEnhancedReqDTO reqDTO,
	                                                              Integer rid, ConscriptDTO conscriptDTO);

	/**
	 * 军队调遣
	 *
	 * @param rid
	 * @param assignDTO
	 * @return
	 */
	LogicTaskResultDTO<WarArmyDTO> assignArmy(Integer rid, AssignDTO assignDTO);

	/**
	 * 军团返回
	 *
	 * @param army
	 */
	void armyBack(WarArmyDTO army);


	/**
	 * 获取地图单元格的NPC军队
	 *
	 * @param posX, posY
	 */
	List<WarArmyDTO> getNpcArmyListByPos(Integer posX, Integer posY);

	/**
	 * 删除NPC军队
	 *
	 * @param toX, toY
	 */
	void removeNPCArmy(Integer toX, Integer toY);

	/**
	 * 放弃驻守
	 *
	 * @param posId
	 */
	void updateGiveUpPosArmy(Integer posId);

	/**
	 * 打断
	 *
	 * @param posId
	 */
	void updateStopInPosArmy(Integer posId);

	/**
	 * 更新 军团 单元格位置
	 *
	 * @param army
	 */
	void checkSyncCell(WarArmyDTO army);
}
