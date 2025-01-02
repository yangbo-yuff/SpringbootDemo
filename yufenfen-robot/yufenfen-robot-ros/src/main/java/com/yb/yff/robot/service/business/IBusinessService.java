package com.yb.yff.robot.service.business;

import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;

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
 * @Class: IRoleService
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description: 角色服务
 */
public interface IBusinessService {
	/**
	 * 分发业务
	 * @param requestDTO
	 */
	GameMessageEnhancedResDTO dispathBusiness(String businessName, GameMessageEnhancedReqDTO requestDTO);
}
