package com.yb.yff.game.data.dto.army;

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
	private Integer aRid;

	/**
	 * 防守方id,0为系统npc
	 */
	private Integer dRid;

	/**
	 * 攻击方战报是否已阅 0:未阅 1:已阅
	 */
	private Boolean aIsRead;

	/**
	 * 攻击方战报是否已阅 0:未阅 1:已阅
	 */
	private Boolean dIsRead;

	/**
	 * 开始攻击方军队
	 */
	private String bAArmy;

	/**
	 * 开始防守方军队
	 */
	private String bDArmy;

	/**
	 * 开始攻击方将领
	 */
	private String bAGeneral;

	/**
	 * 开始防守方将领
	 */
	private String bDGeneral;

	/**
	 * 结束攻击方军队
	 */
	private String eAArmy;

	/**
	 * 结束防守方军队
	 */
	private String eDArmy;

	/**
	 * 结束攻击方将领
	 */
	private String eAGeneral;

	/**
	 * 结束防守方将领
	 */
	private String eDGeneral;

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
