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
public enum FacilityType {
 	Main(0),		//主城
	JiFengYing(1), // 疾风营
	TaiBiYing (2),// 铁壁营
	JiJiYing (3),// 军机营
	ShangWuYing(4), // 尚武营
	BiYing(5),// 兵营
	YuBeiYiSuo(6),// 预备役所
	DianJiangTai_Han(7),// 汉点将台
	DianJiangTai_Wei(8),// 魏点将台
	DianJiangTai_Shu(9),// 蜀点将台
	DianJiangTai_Wu(10),// 吴点将台
	DianJiangTai_Qun(11),// 群点将台
	FenChanTai(12),// 封禅台
	JiaoChang(13),	//校场
	TongShuaiTing(14),	//统帅厅
	JiShi(15),	//集市
	MuBingSuo(16),	//募兵所
	FaMuChang(17),	//伐木场
	LianTieChang(18),	//炼铁场
	MoFang(19),	//磨坊
	CaiShiChang(20),	//采石场
	MingJu(21),	//民居
	SheJiLing(22),	//社稷坛
	ChengQiang(23),	//城墙
	NvChang(24),	//女墙
	ChuangKu(25);	// 仓库

 	private final int value;

	FacilityType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
