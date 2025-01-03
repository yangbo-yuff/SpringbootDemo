package com.yb.yff.game.data.dto.union;

import com.yb.yff.game.data.constant.myEnum.UnionApplyState;
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
 * @Description: 加入联盟申请记录
 */
@Data
public class UnionApplyDTO {
	private Integer id;
	private Integer unionId;
	private Integer rid;
	private Integer state;
	private String nickName;

	public UnionApplyDTO(){}

	public UnionApplyDTO(Integer rid, Integer unionId, String nickName) {
		this.unionId = unionId;
		this.rid = rid;
		this.nickName = nickName;
		this.state = UnionApplyState.UnionUntreated.getValue();
	}
}
