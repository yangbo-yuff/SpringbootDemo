package com.yb.yff.game.business.businessLogic.impl.map;

import com.yb.yff.game.business.businessDataMgr.impl.BuildMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.data.constant.myEnum.BuildType;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.nationMap.MapBuildDTO;
import com.yb.yff.game.data.dto.nationMap.MapCellBaseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @Class: MapPositionLogic
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 区域地图坐标检测
 */
@Service
@Slf4j
public class MapPositionLogic {

	@Autowired
	RoleDataMgrImpl roleDataMgrImpl;

	@Autowired
	BuildMgrImpl buildMgrImpl;

	@Autowired
	CityMgrImpl cityMgrImpl;

	// 是否能到达
	public boolean isCanArrive(int rid, int x, int y) {
		int radius = 0;
		Integer unionId = roleDataMgrImpl.getRoleDTO(rid).getUnionId();

		MapBuildDTO mapBuild = buildMgrImpl.getPositionBuild(x, y);
		if (mapBuild != null) {
			radius = getCellRadius(mapBuild);
		}

		CityDTO city = cityMgrImpl.getPositionCity(x, y);
		if (city != null) {
			radius = getCellRadius(city);
		}

		// 查找10格半径
		for (int tx = x - 10; tx <= x + 10; tx++) {
			for (int ty = y - 10; ty <= y + 10; ty++) {
				MapBuildDTO b1 = buildMgrImpl.getPositionBuild(tx, ty);
				if (b1 != null) {
					int absX = Math.abs(x - tx);
					int absY = Math.abs(y - ty);
					int radius1 = getCellRadius(b1);
					if (absX <= radius + radius1 + 1 && absY <= radius + radius1 + 1) {
						if(b1.getRid() == null || b1.getRid() == 0){
							return true;
						}

						Integer unionId1 = roleDataMgrImpl.getRoleDTO(b1.getRid()).getUnionId();
						Integer parentId = roleDataMgrImpl.getRoleDTO(b1.getRid()).getParentId();
						if (b1.getRid() == rid || (unionId != null && unionId != 0 && (unionId == unionId1 || unionId == parentId))) {
							return true;
						}
					}
				}

				CityDTO c1 = cityMgrImpl.getPositionCity(tx, ty);
				if (c1 != null) {
					int absX = Math.abs(x - tx);
					int absY = Math.abs(y - ty);
					int radius1 = getCellRadius(c1);
					if (absX <= radius + radius1 + 1 && absY <= radius + radius1 + 1) {
						int unionId1 = roleDataMgrImpl.getRoleDTO(c1.getRid()).getUnionId();
						int parentId = roleDataMgrImpl.getRoleDTO(c1.getRid()).getParentId();
						if (c1.getRid() == rid || (unionId != null && unionId != 0 && (unionId == unionId1 || unionId == parentId))) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * 是否能驻守
	 * @param rid
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isCanDefend(int rid, Integer x, Integer y) {
		Integer unionId = roleDataMgrImpl.getRoleDTO(rid).getUnionId();

		MapBuildDTO b = buildMgrImpl.getPositionBuild(x, y);
		if (b != null) {
			if(b.getRid() == null || b.getRid() == 0){
				return false;
			}

			Integer tUnionId = roleDataMgrImpl.getRoleDTO(b.getRid()).getUnionId();
			Integer tParentId = roleDataMgrImpl.getRoleDTO(b.getRid()).getParentId();
			if (b.getRid() == rid) {
				return true;
			} else if (unionId != null && unionId > 0 && tUnionId != null && tUnionId > 0) {
				return tUnionId == unionId;
			} else if (unionId != null && unionId > 0 && tParentId != null && tParentId > 0) {
				return tParentId == unionId;
			}
		}

		CityDTO c = cityMgrImpl.getPositionCity(x, y);
		if (c != null) {
			Integer tUnionId = roleDataMgrImpl.getRoleDTO(c.getRid()).getUnionId();
			Integer tParentId = roleDataMgrImpl.getRoleDTO(c.getRid()).getParentId();
			if (c.getRid() == rid) {
				return true;
			} else if (unionId > 0 && tUnionId > 0) {
				return tUnionId == unionId;
			} else if (unionId > 0 && tParentId > 0) {
				return tParentId == unionId;
			}
		}
		return false;
	}

	/**
	 * 是否是免战
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isWarFree(Integer x, Integer y) {
		MapBuildDTO b = buildMgrImpl.getPositionBuild(x, y);
		if (b != null) {
			return buildMgrImpl.isWarFree(b);
		}

		CityDTO c = cityMgrImpl.getPositionCity(x, y);
		Integer allianceId = roleDataMgrImpl.getRoleDTO(c.getRid()).getUnionId();

		if (c != null && allianceId > 0) {
			return cityMgrImpl.isWarFree(c);
		}
		return false;
	}


	/**
	 * 获取建筑格子半径
	 *
	 * @param mapCell
	 * @return
	 */
	public int getCellRadius(MapCellBaseData mapCell) {
		if (mapCell.getType() == BuildType.MapBuildSysCity.getValue()) {
			if (mapCell.getLevel() >= 8) {
				return 3;
			} else if (mapCell.getLevel() >= 5) {
				return 2;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	public int travelTime(Integer speed, PositionDTO from, PositionDTO to) {
		return travelTime(speed, from.getX(), from.getY(), to.getX(), to.getY());
	}

	public int travelTime(Integer speed, Integer fromX, Integer fromY, Integer toX, Integer  toY) {

		Integer w = Math.abs(fromX - toX);
		Integer h = Math.abs(fromY - toY);
		double dis = Math.sqrt(w * w + h * h) * CityPositionUtils.MapScale;
		double t = dis / speed ;
		return (int)t;
	}
}
