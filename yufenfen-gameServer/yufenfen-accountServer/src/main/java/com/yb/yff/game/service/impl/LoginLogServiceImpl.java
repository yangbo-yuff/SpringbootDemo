package com.yb.yff.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yb.yff.game.data.dto.account.LoginDTO;
import com.yb.yff.game.data.entity.LoginHistoryEntity;
import com.yb.yff.game.data.entity.LoginLastEntity;
import com.yb.yff.game.service.ILoginLogService;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.ResponseDTO;
import org.springframework.beans.BeanUtils;
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
 * @Class: LoginLogServiceImpl
 * @CreatedOn 2024/12/16.
 * @Email: yangboyff@gmail.com
 * @Description: 登录日志服务
 */
@Service
public class LoginLogServiceImpl implements ILoginLogService {
	@Autowired
	LoginHistoryServiceImpl loginHistoryService;

	@Autowired
	LoginLastServiceImpl loginLastService;

	/**
	 * 添加登录日志
	 *
	 * @param log
	 * @return
	 */
	@Override
	public boolean addLoginLog(LoginDTO log) {
		LoginHistoryEntity loginHistoryEntity = new LoginHistoryEntity();
		BeanUtils.copyProperties(log, loginHistoryEntity);

		loginHistoryEntity.setState(log.getIsLogout());

		try {
			Integer result = loginHistoryService.getBaseMapper().insert(loginHistoryEntity);
			if (result > 0) {
				return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 更新最近登录时间
	 *
	 * @param log
	 * @return
	 */
	@Override
	public boolean updateLoginLast(LoginDTO log) {
		LoginLastEntity loginHistoryEntity = new LoginLastEntity();
		BeanUtils.copyProperties(log, loginHistoryEntity);

		try {
			UpdateWrapper<LoginLastEntity> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("uid", loginHistoryEntity.getUid());
			return loginLastService.saveOrUpdate(loginHistoryEntity, updateWrapper);
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据session 获取最近登录信息
	 *
	 * @param Session
	 * @return
	 */
	@Override
	public LoginLastEntity getLoginLastInfoBySession(String Session) {
		QueryWrapper<LoginLastEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("session", Session);

		return loginLastService.getOne(queryWrapper);
	}
}
