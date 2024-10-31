package com.yb.yff.game.utils;

import com.yb.yff.sb.data.dto.city.*;

import java.util.List;
import java.util.Random;

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
 * @Class: CityPositionUtils
 * @CreatedOn 2024/10/27.
 * @Email: yangboyff@gmail.com
 * @Description: 城市位置分配工具
 */
public class CityPositionUtils {

	/**
	 * 主城安全距离，即新主城与已有主城的距离不能小于该值，否则会重新分配
	 * 主城与世界尽头 距离不能小于 该值/2
	 */
	public static int SafeDistance = 10;

	/**
	 * 地图宽度
	 */
	public static int MapWith = 200;

	/**
	 * 地图高度
	 */
	public static int MapHeight = 200;

	public static int MaxCreateCount = 100 * MapWith * MapHeight;

	/**
	 * 随机获取一个坐标，用于生成主城，预留与世界尽头的距离，避免后续判断浪费资源
	 *
	 * @return
	 */
	public static PositionDTO getCityPositionRandom() {
		Random random = new Random();
		PositionDTO cityPosition = new PositionDTO();
		cityPosition.setX(random.nextInt(SafeDistance / 2, MapWith - SafeDistance / 2));
		cityPosition.setY(random.nextInt(SafeDistance / 2, MapHeight - SafeDistance / 2));
		return cityPosition;
	}

	/**
	 * 将坐标转换为地图编号
	 *
	 * @param CityPos
	 * @return
	 */
	public static int position2Number(PositionDTO CityPos) {
		return CityPos.getX() + MapWith * CityPos.getY();
	}


	/**
	 * 将地图编号转换为坐标
	 *
	 * @param cityNum
	 * @return
	 */
	public static PositionDTO number2Position(Integer cityNum) {
		int x = cityNum * MapWith;
		int y = x / MapWith;

		PositionDTO position = new PositionDTO();
		position.setX(x);
		position.setY(y);

		return position;
	}

	/**
	 * 获得合适建城的坐标
	 * @param citys 已有的玩家城池
	 * @return
	 */
	public static PositionDTO getIdealPosition(List<CityData> citys, NationalMaps nationalMaps) {
		int createdCount = 0;
		while (true) {
			// 超出最大次数，返回null
			if(createdCount > MaxCreateCount){
				return null;
			}

			PositionDTO cityPosition = getCityPositionRandom();

			int index = position2Number(cityPosition);
			NationalMap nationalMap = nationalMaps.getNMaps().get(index);

			if(checkPosition(citys, nationalMap, cityPosition)){
				return cityPosition;
			}

			createdCount++;
		}
	}

	private static boolean checkPosition(List<CityData> citys, NationalMap nationalMap, PositionDTO cityPosition){
		//系统城池附近10格不能有玩家城池
		for (CityData cityData : citys) {
			CityDTO city = cityData.getCityList().get(0);
			if(city == null){
				continue;
			}

			if (Math.abs(cityPosition.getX() - city.getX()) <= SafeDistance
					&& Math.abs(cityPosition.getY() - city.getY()) <= SafeDistance) {
				return false;
			}
		}

		if(nationalMap == null){
			return false;
		}
		if(nationalMap.getType() == 0 ){
			return false;
		}

		return true;
	}
}
