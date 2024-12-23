package com.yb.yff.game.data.dto.war;

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
 * @Class: RealBattleAttr
 * @CreatedOn 2024/11/27.
 * @Email: yangboyff@gmail.com
 * @Description: 真正的战斗属性
 */
@Data
public class RealBattleAttr {
 private Integer force;     //武力
 private Integer strategy;  //策略
 private Integer defense;   //防御
 private Integer speed;     //速度
 private Integer destroy;   //破坏
}
