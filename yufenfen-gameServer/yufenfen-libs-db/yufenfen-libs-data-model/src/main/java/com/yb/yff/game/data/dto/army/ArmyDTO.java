package com.yb.yff.game.data.dto.army;

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
 * @Class: ArmyResDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 军队DTO
 */
@Data
public class ArmyDTO {
	private Integer id;
	private Integer rid;
	private Integer cityId;
	private Integer order;
	private List<Integer> generals;
	private List<Integer> soldiers;
	private List<Integer> conCnts;
	private List<Integer> conTimes;
	/**
	 *  0:空闲 1:攻击 2：驻军 3:返回
	 */
	private Integer cmd;
	private Integer fromX;
	private Integer fromY;
	private Integer toX;
	private Integer toY;
	private Long start;
	private Long end;
	private Integer state;
	private Integer unionId;

	public void  setFrom(Integer fromX, Integer fromY){
		this.fromX = fromX;
		this.fromY = fromY;
	}

	public void setTo(Integer toX, Integer toY){
		this.toX = toX;
		this.toY = toY;
	}

	public void setFromTo(Integer fromX, Integer fromY, Integer toX, Integer toY){
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	public void swapFromTo(){
		Integer oldToX = toX;
		Integer oldToY = toY;

		this.toX = this.fromX;
		this.toY =this.fromY;

		this.fromX = oldToX;
		this.fromY = oldToY;
	}
}
