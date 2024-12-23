package com.yb.yff.game.business.businessLogic.impl;

import com.yb.yff.game.business.businessDataMgr.impl.CityFacilityMgrImpl;
import com.yb.yff.game.business.businessLogic.ICityFacilityLogic;
import com.yb.yff.game.data.dto.facility.UpFacilityTaskTCTP;
import com.yb.yff.game.data.dto.city.FacilityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @Class: CityFacilityLogicImpl
 * @CreatedOn 2024/11/9.
 * @Email: yangboyff@gmail.com
 * @Description: 主城设施相关业务逻辑
 */
@Service
public class CityFacilityLogicImpl implements ICityFacilityLogic {
	@Autowired
	CityFacilityMgrImpl cityFacilityMgrImpl;

	/**
	 * 更新设施， 升级设施完成后，更新设施
	 *
	 * @param upFParam
	 * @return
	 */
	@Override
	public boolean updataFacility(UpFacilityTaskTCTP upFParam) {
		FacilityDTO facilityDTO = cityFacilityMgrImpl.getFacility(upFParam.getRid(),  upFParam.getCityId(), upFParam.getFType());
		facilityDTO.setLevel(upFParam.getFacilityPropertyLevel().getLevel());
		facilityDTO.setUp_time(0);

		return cityFacilityMgrImpl.updateFacilities2DB(upFParam.getRid(),  upFParam.getCityId());
	}
}
