package com.yb.yff.game.data.dto.war;

import com.yb.yff.game.jsondb.data.dto.skill.Skill;
import lombok.Data;

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
 * @Class: AttachSkill
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗技能
 */
@Data
public class AttachSkill {
	private Skill cfg;
	private int id;
	private int lv;
	private int duration; // 剩余轮数
	private boolean isEnemy;

	public AttachSkill(Skill cfg, int id, int lv) {
		this.cfg = cfg;
		this.id = id;
		this.lv = lv;
		this.duration = cfg.getDuration();
	}
}
