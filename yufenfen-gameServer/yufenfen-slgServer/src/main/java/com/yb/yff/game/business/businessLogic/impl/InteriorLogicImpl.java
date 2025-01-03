package com.yb.yff.game.business.businessLogic.impl;

import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessLogic.IInteriorLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.data.dto.interior.CollectResDTO;
import com.yb.yff.game.data.dto.interior.OpenCollectResDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.jsondb.data.dto.BasicRole;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
@Service
@Slf4j
public class InteriorLogicImpl implements IInteriorLogic {
	@Autowired
	RoleDataMgrImpl roleDataMgrImpl;

	@Autowired
	IRoleLogic roleLogic;

	/**
	 * 征收
	 * @param rid
	 * @param collectResDTO
	 * @return
	 */
	@Override
	public ResponseCode roleCollect(Integer rid, CollectResDTO collectResDTO) {
		RoleDTO role = roleLogic.getRole(rid);
		if (role == null){
			return NetResponseCodeConstants.DBError;
		}

		BasicRole roleConfig = roleDataMgrImpl.getRoleConfig();

		if(role.getCollectTimes() >= roleConfig.getCollect_times_limit()){
			return NetResponseCodeConstants.OutCollectTimesLimit;
		}

		Long lastCollectTime = role.getLastCollectTime().getTime();
		Long needTime = lastCollectTime + roleConfig.getCollect_interval() * 1000;

		if(needTime > System.currentTimeMillis()){
			return NetResponseCodeConstants.InCdCanNotOperate;
		}

		// 按 5倍 资源产量进行一次征收
		roleLogic.roleCollect(5);

		// 更新缓存
		role.setLastCollectTime(new Date());
		role.setCollectTimes(role.getCollectTimes() + 1);

		// 更新数据库
		RoleDTO updateRole = new RoleDTO();
		updateRole.setRid(rid);
		updateRole.setCollectTimes(role.getCollectTimes());
		updateRole.setLastCollectTime(role.getLastCollectTime());
		roleDataMgrImpl.updateRoleDataToDB(updateRole);

		Integer collectGold = 5 * roleLogic.getRoleResource(rid).getGrainYield();
		collectResDTO.setGold(collectGold);

		getCollectInfo(rid, collectResDTO);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 征收
	 *
	 * @param rid
	 * @param collectResDTO
	 * @return
	 */
	@Override
	public ResponseCode roleOpenCollect(Integer rid, OpenCollectResDTO collectResDTO) {
		getCollectInfo(rid, collectResDTO);

		return NetResponseCodeConstants.SUCCESS;
	}

	private void getCollectInfo(Integer rid, OpenCollectResDTO collectResDTO) {
		RoleDTO role = roleLogic.getRole(rid);
		if (role == null){
			return;
		}

		BasicRole roleConfig = roleDataMgrImpl.getRoleConfig();

		collectResDTO.setCurTimes(role.getCollectTimes());
		collectResDTO.setLimit(roleConfig.getCollect_times_limit());

		if(role.getCollectTimes() < roleConfig.getCollect_times_limit()){
			Long nextTime = role.getLastCollectTime().getTime() / 1000 + roleConfig.getCollect_interval();
			collectResDTO.setNextTime(nextTime.intValue());
		}
	}
}
