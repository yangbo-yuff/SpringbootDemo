package com.yb.yff.game.data.constant.myEnum;

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
 * @Class: FacilityType
 * @CreatedOn 2024/11/17.
 * @Email: yangboyff@gmail.com
 * @Description: 设施类型
 */
public enum FacilityAdditionType {
	TypeDurable(1),    //耐久
	TypeCost(2),
	TypeArmyTeams(3),    //队伍数量
	TypeSpeed(4),    //速度
	TypeDefense(5),    //防御
	TypeStrategy(6),    //谋略
	TypeForce(7),    //攻击武力
	TypeConscriptTime(8), //征兵时间
	TypeReserveLimit(9), //预备役上限
	TypeUnkonw(10),
	TypeHanAddition(11),
	TypeQunAddition(12),
	TypeWeiAddition(13),
	TypeShuAddition(14),
	TypeWuAddition(15),
	TypeDealTaxRate(16),  //交易税率
	TypeWood(17),
	TypeIron(18),
	TypeGrain(19),
	TypeStone(20),
	TypeTax(21),  //税收
	TypeExtendTimes(22),  //扩建次数
	TypeWarehouseLimit(23),  //仓库容量
	TypeSoldierLimit(24),  //带兵数量
	TypeVanguardLimit(25);  //前锋数量

	private final int value;

	FacilityAdditionType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
