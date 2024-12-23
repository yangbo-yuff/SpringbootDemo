package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.facility.UpFacilityTaskTCTP;

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
 * @Class: ICityFacilityLogic
 * @CreatedOn 2024/11/9.
 * @Email: yangboyff@gmail.com
 * @Description: 主城设施相关业务逻辑
 */
public interface ICityFacilityLogic {

	/**
	 * 更新设施， 升级设施完成后，更新设施
	 * @param upFParam
	 * @return
	 */
	boolean updataFacility(UpFacilityTaskTCTP upFParam);
}
