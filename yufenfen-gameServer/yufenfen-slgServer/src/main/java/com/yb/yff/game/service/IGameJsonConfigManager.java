package com.yb.yff.game.service;

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
 * @Class: IGameJsonConfigManager
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏JSon配置管理，从Resource 加载配置文件，并缓存到内存中,对外提供管理接口
 */
public interface IGameJsonConfigManager {

	/**
	 * 读取JSON配置
	 * @param clazz
	 * @return
	 */
	<T> T getJsonConfig(Class<T> clazz);
}
