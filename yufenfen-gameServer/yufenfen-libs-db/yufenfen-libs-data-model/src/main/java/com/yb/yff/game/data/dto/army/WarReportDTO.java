package com.yb.yff.game.data.dto.army;

import com.yb.yff.game.data.dto.general.GeneralDTO;
import lombok.Data;

import java.util.Date;
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
 * @Class: WarReportDTO
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗报告
 */
@Data
public class WarReportDTO {
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 攻击方id
	 */
	private Integer a_rid;

	/**
	 * 防守方id,0为系统npc
	 */
	private Integer d_rid;

	/**
	 * 攻击方战报是否已阅 0:未阅 1:已阅
	 */
	private Boolean a_is_read;

	/**
	 * 攻击方战报是否已阅 0:未阅 1:已阅
	 */
	private Boolean d_is_read;

	/**
	 * 开始攻击方军队
	 */
	private String b_a_army;

	/**
	 * 开始防守方军队
	 */
	private String b_d_army;

	/**
	 * 开始攻击方将领
	 */
	private String b_a_general;

	/**
	 * 开始防守方将领
	 */
	private String b_d_general;

	/**
	 * 结束攻击方军队
	 */
	private String e_a_army;

	/**
	 * 结束防守方军队
	 */
	private String e_d_army;

	/**
	 * 结束攻击方将领
	 */
	private String e_a_general;

	/**
	 * 结束防守方将领
	 */
	private String e_d_general;

	/**
	 * 是否攻占 0:否 1:是
	 */
	private Integer occupy;

	/**
	 * 0失败，1打平，2胜利
	 */
	private Integer result;

	/**
	 * 破坏了多少耐久
	 */
	private Integer destroy;

	/**
	 * 回合战报数据
	 */
	private String rounds;

	/**
	 * x坐标
	 */
	private Integer x;

	/**
	 * y坐标
	 */
	private Integer y;
	private Long ctime;
}
