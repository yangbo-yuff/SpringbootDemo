package com.yb.yff.game.business.businessLogic.impl.war;

import com.yb.yff.game.business.businessDataMgr.impl.ArmyMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityFacilityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.GeneralMgrImpl;
import com.yb.yff.game.data.constant.myEnum.FacilityAdditionType;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.dto.war.ArmyPosition;
import com.yb.yff.game.data.dto.war.WarCamp;

import java.util.ArrayList;
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
 * @Class: WarCampLogic
 * @CreatedOn 2024/11/25.
 * @Email: yangboyff@gmail.com
 * @Description: 阵型逻辑
 */
public class WarCampLogic {
	private CityFacilityMgrImpl cityFacilityMgr;
	private ArmyMgrImpl armyMgr;
	private GeneralMgrImpl generalMgr;
//	private ;

	public WarCampLogic(CityFacilityMgrImpl cityFacilityMgr, ArmyMgrImpl armyMgr, GeneralMgrImpl generalMgr) {
		this.cityFacilityMgr = cityFacilityMgr;
		this.armyMgr = armyMgr;
		this.generalMgr = generalMgr;
	}

	// 初始化军队和武将属性、兵种、加成等
	public void initWarCamp(WarCamp warCamp) {
		WarArmyDTO attack = warCamp.getAttack();
		WarArmyDTO defense = warCamp.getDefense();
		// 城内设施加成
		List<Integer> attackAdds = List.of(0, 0, 0, 0);
		if (attack.getArmy().getCityId() > 0) {
			attackAdds = cityFacilityMgr.getAdditions(attack.getArmy().getRid(), attack.getArmy().getCityId(),
					FacilityAdditionType.TypeForce.getValue(),
					FacilityAdditionType.TypeDefense.getValue(),
					FacilityAdditionType.TypeSpeed.getValue(),
					FacilityAdditionType.TypeStrategy.getValue());
		}

		List<Integer> defenseAdds = List.of(0, 0, 0, 0);
		if (defense.getArmy().getCityId() > 0) {
			defenseAdds = cityFacilityMgr.getAdditions(defense.getArmy().getCityId(),
					FacilityAdditionType.TypeForce.getValue(),
					FacilityAdditionType.TypeDefense.getValue(),
					FacilityAdditionType.TypeSpeed.getValue(),
					FacilityAdditionType.TypeStrategy.getValue());
		}

		// 阵营加成
		List<Integer> aCampAdds = List.of(0);
		Integer aCamp = armyMgr.getCamp(attack.getArmy());
		if (aCamp > 0) {
			aCampAdds = cityFacilityMgr.getAdditions(attack.getArmy().getRid(), attack.getArmy().getCityId(), FacilityAdditionType.TypeHanAddition.getValue() - 1 + aCamp);
		}

		List<Integer> dCampAdds = List.of(0);
		int dCamp = armyMgr.getCamp(defense.getArmy());
		if (dCamp > 0) {
			dCampAdds = cityFacilityMgr.getAdditions(defense.getArmy().getRid(), defense.getArmy().getCityId(), FacilityAdditionType.TypeHanAddition.getValue() - 1 + dCamp);
		}

		List<ArmyPosition> attackPos = new ArrayList<>();
		List<ArmyPosition> defensePos = new ArrayList<>();

		for (int i = 0; i < attack.getGeneralList().size(); i++) {
			GeneralDTO general = attack.getGeneralList().get(i);
			if (general == null) {
				attackPos.add(null);
			} else {
				ArmyPosition pos = new ArmyPosition(
						general,
						attack.getArmy().getSoldiers().get(i),
						generalMgr.getForce(general) + attackAdds.get(0) + aCampAdds.get(0),
						generalMgr.getDefense(general) + attackAdds.get(1) + aCampAdds.get(0),
						generalMgr.getSpeed(general) + attackAdds.get(2) + aCampAdds.get(0),
						generalMgr.getStrategy(general) + attackAdds.get(3) + aCampAdds.get(0),
						generalMgr.getDestroy(general) + aCampAdds.get(0),
						i,
						true
				);
				attackPos.add(pos);
			}
		}

		for (int i = 0; i < defense.getGeneralList().size(); i++) {
			GeneralDTO general = defense.getGeneralList().get(i);
			if (general == null) {
				defensePos.add(null);
			} else {
				ArmyPosition pos = new ArmyPosition(
						general,
						defense.getArmy().getSoldiers().get(i),
						generalMgr.getForce(general) + defenseAdds.get(0) + dCampAdds.get(0),
						generalMgr.getDefense(general) + defenseAdds.get(1) + dCampAdds.get(0),
						generalMgr.getSpeed(general) + defenseAdds.get(2) + dCampAdds.get(0),
						generalMgr.getStrategy(general) + defenseAdds.get(3) + dCampAdds.get(0),
						generalMgr.getDestroy(general) + dCampAdds.get(0),
						i,
						false
				);
				defensePos.add(pos);
			}
		}

		warCamp.setAttackPos(attackPos);
		warCamp.setDefensePos(defensePos);
	}

	// 随机一个目标位置
	public ArmyPosition randArmyPosition(List<ArmyPosition> posList) {
		boolean isEmpty = true;
		for (ArmyPosition armyPosition : posList) {
			if (armyPosition != null && armyPosition.getSoldiers() > 0) {
				isEmpty = false;
				break;
			}
		}

		if (isEmpty) {
			return null;
		}

		Random random = new Random();
		while (true) {
			int r = random.nextInt(100);
			int index = r % posList.size();
			if (posList.get(index) != null && posList.get(index).getSoldiers() > 0) {
				return posList.get(index);
			}
		}

//		return null;
	}

	// 最多2个
	public List<ArmyPosition> randMostTwoArmyPosition(List<ArmyPosition> pos) {
		List<Integer> indices = new ArrayList<>();
		List<ArmyPosition> positions = new ArrayList<>();
		ArmyPosition o1 = randArmyPosition(pos);
		if (o1 != null) {
			indices.add(pos.indexOf(o1));
			positions.add(o1);
			ArmyPosition o2 = randArmyPosition(pos);
			if (o2 != null && !o2.equals(o1)) {
				indices.add(pos.indexOf(o2));
				positions.add(o2);
			}
		}
		return positions;
	}

	// 最多3个
	public List<ArmyPosition> randMostThreeArmyPosition(List<ArmyPosition> pos) {
		List<Integer> indices = new ArrayList<>();
		List<ArmyPosition> positions = new ArrayList<>();
		ArmyPosition o1 = randArmyPosition(pos);
		if (o1 != null) {
			indices.add(pos.indexOf(o1));
			positions.add(o1);
			ArmyPosition o2 = randArmyPosition(pos);
			if (o2 != null && !o2.equals(o1)) {
				indices.add(pos.indexOf(o2));
				positions.add(o2);

				ArmyPosition o3 = randArmyPosition(pos);
				if (o3 != null && !o3.equals(o1) && !o3.equals(o2)) {
					indices.add(pos.indexOf(o3));
					positions.add(o3);
				}
			}
		}
		return positions;
	}

	public List<ArmyPosition> allArmyPosition(List<ArmyPosition> pos) {
		List<Integer> indices = new ArrayList<>();
		List<ArmyPosition> positions = new ArrayList<>();

		for (int index = 0; index < pos.size(); index++) {
			ArmyPosition armyPosition = pos.get(index);
			if (armyPosition != null && armyPosition.getSoldiers() != 0) {
				indices.add(index);
				positions.add(armyPosition);
			}
		}

		return positions;
	}

	public ArmyPosition findByCfgId(List<ArmyPosition> pos, int cfgId) {
		for (ArmyPosition armyPosition : pos) {
			if (armyPosition != null && armyPosition.getGeneral() != null && armyPosition.getGeneral().getCfgId() == cfgId) {
				return armyPosition;
			}
		}
		return null;
	}

	public ArmyPosition findByGiId(List<ArmyPosition> pos, int gId) {
		for (ArmyPosition armyPosition : pos) {
			if (armyPosition != null && armyPosition.getGeneral() != null && armyPosition.getGeneral().getId() == gId) {
				return armyPosition;
			}
		}
		return null;
	}

}
