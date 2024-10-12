package com.yb.yff.game.data;

import com.yb.yff.flux.client.data.dto.ServerInfoDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
 * @Class: GBServerInfos
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏业务服务器信息
 */

@Data
@Component
@ConfigurationProperties(prefix = "game.servers")
public class GBServerInfos {
	/**
	 * WS 游戏逻辑服务器信息列表
	 */
	private List<ServerInfoDTO> games;

	/**
	 * WS 聊天服务器信息列表
	 */
	private List<ServerInfoDTO> chats;

	/**
	 * WS 账户服务器信息列表
	 */
	private List<ServerInfoDTO> accounts;
}
