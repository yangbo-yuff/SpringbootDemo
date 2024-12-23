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
	private List<Integer> con_cnts;
	private List<Integer> con_times;
	/**
	 *  0:空闲 1:攻击 2：驻军 3:返回
	 */
	private Integer cmd;
	private Integer from_x;
	private Integer from_y;
	private Integer to_x;
	private Integer to_y;
	private Long start;
	private Long end;
	private Integer state;
	private Integer union_id;

	public void  setFrom(Integer from_x, Integer from_y){
		this.from_x = from_x;
		this.from_y = from_y;
	}

	public void setTo(Integer to_x, Integer to_y){
		this.to_x = to_x;
		this.to_y = to_y;
	}

	public void setFromTo(Integer from_x, Integer from_y, Integer to_x, Integer to_y){
		this.from_x = from_x;
		this.from_y = from_y;
		this.to_x = to_x;
		this.to_y = to_y;
	}
	public void swapFromTo(){
		Integer oldToX = to_x;
		Integer oldToY = to_y;

		this.to_x = this.from_x;
		this.to_y =this.from_y;

		this.from_x = oldToX;
		this.from_y = oldToY;
	}
}
