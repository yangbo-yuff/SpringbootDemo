package com.yb.yff.game.business.businessLogic;

import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.game.data.dto.interior.CollectResDTO;
import com.yb.yff.game.data.dto.interior.OpenCollectResDTO;

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
 * @Class: IInteriorLogic
 * @CreatedOn 2024/11/14.
 * @Email: yangboyff@gmail.com
 * @Description: 内务
 */
public interface IInteriorLogic {
	/**
	 * 征收
	 * @param rid
	 * @param collectResDTO
	 * @return
	 */
	ResponseCode roleCollect(Integer rid, CollectResDTO collectResDTO);

	/**
	 * 征收
	 * @param rid
	 * @param collectResDTO
	 * @return
	 */
	ResponseCode roleOpenCollect(Integer rid, OpenCollectResDTO collectResDTO);
}
