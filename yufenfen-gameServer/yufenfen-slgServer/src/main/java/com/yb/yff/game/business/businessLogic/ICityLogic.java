package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.FacilityResDTO;
import com.yb.yff.game.data.dto.city.UpFacilityDTO;
import com.yb.yff.sb.taskCallback.TimeConsumingTask;

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
 * @Class: ICityLogic
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 主城业务逻辑
 */
public interface ICityLogic {
	/**
	 * 获取角色-主城-设施
	 *
	 * @param facilityResDTO
	 * @param rid
	 * @param cityID
	 * @return
	 */
	ResponseCode getRoleCityFacilities(FacilityResDTO facilityResDTO, Integer rid, Integer cityID);


	/**
	 * 创建设备升级任务
	 *
	 * @param reqDTO
	 * @param roleId
	 * @param upFacilityDTO
	 * @return
	 */
	LogicTaskResultDTO<TimeConsumingTask> createUpFacilityTCT(GameMessageEnhancedReqDTO reqDTO, Integer roleId, UpFacilityDTO upFacilityDTO);

	/**
	 * 安全创建城市
	 *
	 * @param rid
	 */
	boolean createCitySafely(Integer rid);

	/**
	 * 获取角色主城
	 */
	CityDTO getMainCitys(Integer rid);
}
