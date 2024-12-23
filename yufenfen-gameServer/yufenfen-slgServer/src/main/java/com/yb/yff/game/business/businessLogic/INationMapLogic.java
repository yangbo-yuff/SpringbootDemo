package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.nationMap.*;
import com.yb.yff.sb.constant.ResponseCode;

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
 * @Class: getNationalMapConfig
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 获取区域地图配置
 */
public interface INationMapLogic {
	/**
	 * 获取区域地图配置
	 *
	 * @return
	 */
	List<ConfigDTO> getNationalMapConfig();


	/**
	 * 给定位置范围内的建筑物列表
	 *
	 * @param x        X坐标
	 * @param y        Y坐标
	 * @param boundary 边界
	 * @return
	 */
	<T extends MapCellBaseData> List<T> scan(int x, int y, int boundary, Class<T> clazz);

	/**
	 * 给定位置并指定范围和方向内的建筑物列表
	 *
	 * @param x
	 * @param y
	 * @param length
	 * @return
	 */
	<T extends MapCellBaseData> List<T> scanBlock(int x, int y, int length, Class<T> clazz);

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	ResponseCode giveUp(int rid, int x, int y);

	/**
	 * 创建建筑
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	ResponseCode build(MapCellReqDTO buildPos, Integer rid);

	/**
	 * 建筑升级
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	LogicTaskResultDTO<BuildDTO> upBbuild(MapBuildUpDTO buildPos, Integer rid);

	/**
	 * 建筑升级
	 *
	 * @param buildPos
	 * @param rid
	 * @return
	 */
	LogicTaskResultDTO<BuildDTO> delBbuild(MapBuildUpDTO buildPos, Integer rid);

	/**
	 * 判断是否是当前角色的
	 *
	 * @param rid
	 * @param x
	 * @param y
	 * @return
	 */
	boolean buildIsRId(Integer rid, Integer x, Integer y);
}
