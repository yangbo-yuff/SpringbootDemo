package com.yb.yff.game.data.dto.war;

import com.yb.yff.game.data.dto.army.WarArmyDTO;
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
 * @Class: WarCamp
 * @CreatedOn 2024/11/26.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗阵营数据
 */
@Data
public class WarCamp {
	// 攻方
	private WarArmyDTO attack;
	// 守方
	private WarArmyDTO defense;
	// 攻方军团坑位战斗属性
	private List<ArmyPosition> attackPos;
	// 守方军团坑位战斗属性
	private List<ArmyPosition> defensePos;

	public WarCamp() {
	}

	public WarCamp(WarArmyDTO attack, WarArmyDTO defense) {
		this.attack = attack;
		this.defense = defense;
	}
}
