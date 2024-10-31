package com.yb.yff.sb.data.dto.role;

import lombok.Data;

import java.util.Date;

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
 * @Class: RoleAttr
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description: 角色属性
 */
@Data
public class RoleAttrDTO extends RoleData {
	/**
	 * roleId
	 */
	private Integer rid;
	/**
	 * 上级联盟id
	 */
	private Byte parentId;

	/**
	 * 征收次数
	 */
	private Byte collectTimes;

	/**
	 * 最后征收时间
	 */
	private Date lastCollectTime;

	/**
	 * 收藏的位置
	 */
	private String posTags;
}
