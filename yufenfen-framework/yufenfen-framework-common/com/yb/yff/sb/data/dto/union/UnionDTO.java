package com.yb.yff.sb.data.dto.union;

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
 * @Class: union
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：联盟
 */
@Data
public class UnionDTO {
	private Integer cnt;
	private Integer id;
	private List<MemberDTO> major;
	private String name;
	private String notice;
}
