package com.yb.yff.game.data.dto.union;

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
 * @Class: UnionLogDTO
 * @CreatedOn 2024/12/13.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟日志
 */
@Data
public class UnionLogDTO {
	private Integer unionId;
	private Integer op_rid;
	private Integer target_id;
	private Integer state;
	private String des;
	private Long ctime;

	public UnionLogDTO(){}

	public UnionLogDTO(Integer unionId, Integer op_rid, Integer target_id, Integer state, String des, Long ctime) {
		this.unionId = unionId;
		this.op_rid = op_rid;
		this.target_id = target_id;
		this.state = state;
		this.des = des;
		this.ctime = ctime;
	}
}
