package com.yb.yff.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.yff.game.data.dto.account.*;
import com.yb.yff.game.data.entity.LoginLastEntity;
import com.yb.yff.game.data.entity.UserInfoEntity;
import com.yb.yff.game.service.IAccountService;
import com.yb.yff.game.service.ILoginLogService;
import com.yb.yff.game.service.IUserInfoService;
import com.yb.yff.game.utils.AccountUtils;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.ResponseDTO;
import com.yb.yff.sb.utils.SessionUtil;
import com.yb.yff.sb.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.yb.yff.sb.constant.NetResponseCodeConstants.*;

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
 * @Class: AccountServiceImpl
 * @CreatedOn 2024/10/8.
 * @Email: yangboyff@gmail.com
 * @Description: 账户服务
 */
@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {
	@Autowired
	IUserInfoService userInfoService;

	@Autowired
	ILoginLogService loginLogService;

	@Override
	public ResponseCode doValidateToken(String token) {
		// token信息验证
//		if (!JwtUtil.validateToken(token)) {
//			return SessionInvalid;
//		}

		return SUCCESS;
	}

	@Override
	public ResponseDTO doLogin(LoginReqDTO loginInfo) {
		// TODO 因为客户端使用的是MD5加密，所以暂时使用MD5方案，后续调整为JWT方案
		ResponseDTO resDTO = doLoginByMD5(loginInfo);

		// 成功 记录登陆日志
		if(resDTO.getCode().equals(SUCCESS.getCode())){
			LoginDTO loginDTO = new LoginDTO();
			LoginResDTO LoginRes = (LoginResDTO) resDTO.getData();
			BeanUtils.copyProperties(LoginRes, loginDTO);
			loginDTO.setLoginTime(new Date());
			loginDTO.setIsLogout(0);
			loginDTO.setHardware(loginInfo.getHardware());

			loginLogService.addLoginLog(loginDTO);
			loginLogService.updateLoginLast(loginDTO);
		}

		return resDTO;
	}

	@Override
	public ResponseDTO doReLogin(ReLoginReqDTO loginInfo) {
		LoginLastEntity loginLastEntity = loginLogService.getLoginLastInfoBySession(loginInfo.getSession());
		if(loginLastEntity == null){
			ResponseDTO.error(SessionInvalid);
		}

		LoginResDTO resDTO = new LoginResDTO();
		Integer uid = loginLastEntity.getId();
		resDTO.setUid(uid);
		resDTO.setSession(loginInfo.getSession());

		return ResponseDTO.success(resDTO);
	}

	/**
	 * @param loginInfo
	 * @return
	 */
	@Override
	public ResponseDTO doLogout(LogoutDTO loginInfo) {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setLogoutTime(new Date());
		loginDTO.setIsLogout(1);
		loginDTO.setUid(loginInfo.getUid());

		loginLogService.addLoginLog(loginDTO);
		loginLogService.updateLoginLast(loginDTO);

		return ResponseDTO.success(null);
	}

	@Override
	public ResponseCode doRegiter(RegisterDTO registerDTO) {
		// TODO 因为客户端使用的是MD5加密，所以暂时使用MD5方案，后续调整为JWT方案
		return doRegiterByMD5(registerDTO);
	}

	public ResponseDTO doLoginByMD5(UserInfoDTO loginDTO){
		log.info("doLoginByMD5 user info: " + loginDTO.getUsername());

		if (!checkUser(loginDTO)) {
			return ResponseDTO.error(UserNotExist);
		}

		UserInfoEntity userInfo = new UserInfoEntity();
		userInfo.setUsername(loginDTO.getUsername());

		QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>(userInfo);
		UserInfoEntity targetUerInfo = userInfoService.getOne(queryWrapper);

		// 检测登陆者密码
		String passMD5 = AccountUtils.encodeMD5(loginDTO.getPassword() + targetUerInfo.getPasscode());

		if(!passMD5.equals(targetUerInfo.getPasswd())){
			return ResponseDTO.error(PwdIncorrect);
		}

		LoginResDTO resDTO = new LoginResDTO();
		BeanUtils.copyProperties(loginDTO, resDTO);
		Integer uid = targetUerInfo.getId();
		resDTO.setUid(uid);
		resDTO.setSession(SessionUtil.newSessionStr(uid));

		return ResponseDTO.success(resDTO);
	}

	public ResponseCode doRegiterByMD5(RegisterDTO registerDTO){
		log.info("doRegiterByMD5 user info: " + registerDTO.getUsername());
		if (checkUser(registerDTO)) {
			return UserExist;
		}

		log.info("doRegiterByMD5 start crate user: " + registerDTO.getUsername());

		// 8位随机字符串
		String passcode = StringUtils.createStrBySize(8);

		UserInfoEntity userInfo = new UserInfoEntity();

		String passMD5 = AccountUtils.encodeMD5(registerDTO.getPassword() + passcode);

		userInfo.setPasswd(passMD5);
		userInfo.setUsername(registerDTO.getUsername());
		userInfo.setPasscode(passcode);

		if (userInfoService.getBaseMapper().insert(userInfo) > 0) {
			return SUCCESS;
		}

		return DBError;
	}

	public boolean checkUser(UserInfoDTO userInfoDTO) {
		UserInfoEntity userInfo = new UserInfoEntity();

		userInfo.setUsername(userInfoDTO.getUsername());

		QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>(userInfo);

//		if (userInfoService.exists(queryWrapper)) {
		if (userInfoService.count(queryWrapper) > 0) {
			return true;
		}

		return false;
	}
}
