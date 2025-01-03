package com.yb.yff.game.data.dto.union;

import com.yb.yff.game.data.constant.myEnum.UnionState;
import lombok.Data;

import java.util.ArrayList;
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
 * @Class: UnionResDTO
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟数据
 */
@Data
public class UnionDTO {
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 联盟名字
	 */
	private String name;

	/**
	 * 成员 role id
	 */
	private List<Integer> members;

	/**
	 * 创建者id
	 */
	private Integer createId;

	/**
	 * 盟主
	 */
	private Integer chairman;

	/**
	 * 副盟主
	 */
	private Integer viceChairman;

	/**
	 * 公告
	 */
	private String notice;

	/**
	 * 0解散，1运行中
	 */
	private Integer state;

	/**
	 * 成员 列表
	 */
	private List<MemberDTO> memberList;


	public UnionDTO() {
	}

	public UnionDTO(String uName, Integer createId) {
		this.name = uName;
		this.createId = createId;
		this.chairman = createId;
		this.members = new ArrayList<>();
		members.add(createId);
		this.state = UnionState.UnionRunning.getValue();
		this.notice = "";
	}
}
