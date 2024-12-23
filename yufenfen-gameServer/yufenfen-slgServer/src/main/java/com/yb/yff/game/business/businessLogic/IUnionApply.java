package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.union.UnionApplyDTO;

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
 * @Class: IUnionApply
 * @CreatedOn 2024/12/15.
 * @Email: yangboyff@gmail.com
 * @Description: 审核接口
 */
public interface IUnionApply {
	/**
	 * 审核拒绝
	 */
	void onUnionRefuse(UnionApplyDTO apply);

	/**
	 * 审核通过
	 */
	void onUnionAdopt(UnionApplyDTO apply);
}
