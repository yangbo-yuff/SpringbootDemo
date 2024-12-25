package com.yb.yff.game.data.dto.war;

import com.yb.yff.game.data.dto.general.GeneralDTO;
import lombok.Data;

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
 * @Class: ArmyPosition
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 军团坑位战斗属性
 */
@Data
public class ArmyPosition {
	private GeneralDTO general;
	private int soldiers;
	private int force;
	private int defense;
	private int speed;
	private int strategy;
	private int destroy;
	private int arms;
	private int position;
	private boolean isAttack; // true-攻城方，false-守城方
	private List<AttachSkill> skills;

	public ArmyPosition(GeneralDTO general, int soldiers, int force, int defense, int speed, int strategy, int destroy,
	                    int position, boolean isAttack) {
		this.general = general;
		this.soldiers = soldiers;
		this.force = force;
		this.defense = defense;
		this.speed = speed;
		this.strategy = strategy;
		this.destroy = destroy;
		this.arms = general.getArms();
		this.position = position;
		this.isAttack = isAttack;
	}
}
