package com.yb.yff.game.data.bo;

import com.yb.yff.game.data.dto.war.HitDTO;
import com.yb.yff.game.data.dto.war.WarCamp;
import com.yb.yff.game.data.dto.war.WarRoundDTO;
import com.yb.yff.game.data.dto.war.WarRoundIsEndDTO;
import lombok.Data;

import java.util.ArrayList;
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
 * @Class: WarResultBO
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗结果
 */
@Data
public class WarResultBO {
	private WarCamp camp;
	private List<WarRoundIsEndDTO> rounds;
	private WarRoundIsEndDTO curRound;
	private int result; // 0: 失败, 1: 平局, 2: 胜利

	public WarResultBO(WarCamp camp) {
		this.camp = camp;
		this.rounds = new ArrayList<>();
	}

	public List<WarRoundDTO> getWarRounds() {
		List<WarRoundDTO> warRouds = new ArrayList<>();
		rounds.forEach(warRoundIsEndDTO -> {
			WarRoundDTO warRound = new WarRoundDTO();
			warRound.setB(warRoundIsEndDTO.getB());
			warRouds.add(warRound);
		});
		return warRouds;
	}
}
