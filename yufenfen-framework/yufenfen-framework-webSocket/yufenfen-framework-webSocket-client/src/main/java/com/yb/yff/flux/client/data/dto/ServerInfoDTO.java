package com.yb.yff.flux.client.data.dto;

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
 * @Class: ServerInfoDTO
 * @CreatedOn 2024/10/12.
 * @Email: yangboyff@gmail.com
 * @Description: 一般服务器信息 name+url
 */
@Data
public class ServerInfoDTO {
	/**
	 * 服务器节点名
	 */
	private String nodeName;
	/**
	 * 服务器节点链接URL
	 */
	private String node;
}
