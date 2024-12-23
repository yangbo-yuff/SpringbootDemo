package com.yb.yff.game.data.dto.army;

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
 * @Class: NpcArmyDTO
 * @CreatedOn 2024/11/26.
 * @Email: yangboyff@gmail.com
 * @Description: NPC 军团，即没被任何玩家征服的领土的独立军团
 */
@Data
public class WarArmyDTO {
	private ArmyDTO army;

	private Integer cellX;
	private Integer cellY;

	private List<GeneralDTO> generalList;
}
