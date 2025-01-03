package com.yb.yff.game.service;

import com.yb.yff.game.data.dto.account.LoginDTO;
import com.yb.yff.game.data.entity.LoginLastEntity;

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
 * @Class: IAccountService
 * @CreatedOn 2024/10/8.
 * @Email: yangboyff@gmail.com
 * @Description: 账户登录日志接口
 */
public interface ILoginLogService {
	/**
	 * 添加登录日志
	 * @param log
	 * @return
	 */
	boolean addLoginLog(LoginDTO log);

	/**
	 * 更新最近登录时间
	 * @param log
	 * @return
	 */
	boolean updateLoginLast(LoginDTO log);

	/**
	 * 根据session 获取最近登录信息
	 * @param Session
	 * @return
	 */
	LoginLastEntity getLoginLastInfoBySession(String Session);
}
