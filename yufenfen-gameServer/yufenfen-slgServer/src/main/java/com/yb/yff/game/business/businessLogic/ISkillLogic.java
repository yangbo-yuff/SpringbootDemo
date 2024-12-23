package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.skill.SkillDTO;
import com.yb.yff.game.data.dto.skill.SkillLevelDTO;

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
 * @Class: ISkillLogic
 * @CreatedOn 2024/12/19.
 * @Email: yangboyff@gmail.com
 * @Description: 技能逻辑处理
 */
public interface ISkillLogic {
	LogicTaskResultDTO<List<SkillDTO>> getSkills(Integer rid);
}
