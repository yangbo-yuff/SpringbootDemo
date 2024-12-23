package com.yb.yff.game.utils;

import com.yb.yff.game.data.dto.city.PositionDTO;

import java.awt.geom.Point2D;

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
 * @Class: PositionUtils
 * @CreatedOn 2024/11/24.
 * @Email: yangboyff@gmail.com
 * @Description: 2D 位置 工具
 */
public class PositionUtils {

	/**
	 * 比较两个坐标是否相等
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean equalsPosition(PositionDTO p1, PositionDTO p2) {
		return p1.getX() == p2.getX() && p1.getY() == p2.getY();
	}

	/**
	 * 比较两个坐标是否相等
	 * @param p1
	 * @param p2x
	 * @param p2y
	 * @return
	 */
	public static boolean equalsPosition(PositionDTO p1, Integer p2x, Integer p2y) {
		return p1.getX() == p2x && p1.getY() == p2y;
	}

	/**
	 * 比较两个坐标是否相等
	 * @param p1x
	 * @param p1y
	 * @param p2
	 * @return
	 */
	public static boolean equalsPosition(Integer p1x, Integer p1y, PositionDTO p2) {
		return p1x == p2.getX() && p1y == p2.getY();
	}

	/**
	 * 比较两个坐标是否相等
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @return
	 */
	public static boolean equalsPosition(Integer p1x, Integer p1y, Integer p2x, Integer p2y) {
		return p1x == p2x && p1y == p2y;
	}

	/**
	 * 计算两点之间的距离
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(PositionDTO p1, PositionDTO p2) {
		Point2D p2d1 =new Point2D.Double(p1.getX(), p1.getY());
		Point2D p2d2 = new Point2D.Double(p2.getX(), p2.getY());

		return p2d1.distance(p2d2);
	}

	/**
	 * 计算两点之间的角度
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double angleBetweenPoints(PositionDTO p1, PositionDTO p2) {
		return Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
	}

	/**
	 * 计算两点之间的中点坐标
	 */
	public static PositionDTO ratePosition(PositionDTO p1, PositionDTO p2, Double rate) {
		return ratePosition(p1.getX(), p1.getY(), p2.getX(), p2.getY(), rate);
	}

	/**
	 * 计算两点之间的中点坐标
	 */
	public static PositionDTO ratePosition(Integer p1x, Integer p1y, Integer p2x, Integer p2y, Double rate) {
		Double midX = (p1x + p2x) * rate;
		Double midY = (p1y + p2y) * rate;
		return new PositionDTO(midX.intValue(), midY.intValue());
	}
}
