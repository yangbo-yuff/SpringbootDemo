package com.yb.yff.game.business.businessLogic.impl;

import com.yb.yff.game.business.businessDataMgr.impl.SKillMgrImpl;
import com.yb.yff.game.business.businessLogic.ISkillLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.skill.SkillDTO;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
 * @Class: SkillLogicImpl
 * @CreatedOn 2024/12/19.
 * @Email: yangboyff@gmail.com
 * @Description: 技能相关逻辑处理
 */
@Service
@Slf4j
public class SkillLogicImpl extends BusinessDataSyncImpl<SkillDTO> implements ISkillLogic {
	@Autowired
	SKillMgrImpl skillMgr;

	@Autowired
	IPushService pushService;

	@PostConstruct
	public void init() {
		super.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_skill, pushService);
	}

	/**
	 * @param rid
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<SkillDTO>> getSkills(Integer rid) {
		LogicTaskResultDTO<List<SkillDTO>> result = new LogicTaskResultDTO<>();

		List<SkillDTO> skills = skillMgr.getRoleSkills(rid);
		if(skills == null){
			result.setCode(NetResponseCodeConstants.DBError);
			return result;
		}

		result.setResult(skills);

		result.setCode(NetResponseCodeConstants.SUCCESS);
		return result;
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param skillDTO
	 */
	@Override
	public void syncExecute(Integer rid, SkillDTO skillDTO) {
		skillMgr.saveOrUpdateSkill2DB(skillDTO);

		this.pushData(rid, skillDTO);
	}
}
