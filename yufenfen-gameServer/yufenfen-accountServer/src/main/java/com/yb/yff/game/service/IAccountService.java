package com.yb.yff.game.service;

import com.yb.yff.game.data.dto.account.LoginReqDTO;
import com.yb.yff.game.data.dto.account.LogoutDTO;
import com.yb.yff.game.data.dto.account.ReLoginReqDTO;
import com.yb.yff.game.data.dto.account.RegisterDTO;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.ResponseDTO;

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
 * @Description: 账户服务接口
 */
public interface IAccountService {
	ResponseCode doValidateToken(String token);

	ResponseDTO doLogin(LoginReqDTO loginInfo);

	ResponseDTO doReLogin(ReLoginReqDTO loginInfo);

	ResponseDTO doLogout(LogoutDTO loginInfo);

	ResponseCode doRegiter(RegisterDTO registerDTO);
}
