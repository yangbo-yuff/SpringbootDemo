package com.yb.yff.game.business.businessLogic.impl.war;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.ArmyMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityFacilityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.GeneralMgrImpl;
import com.yb.yff.game.data.bo.WarResultBO;
import com.yb.yff.game.data.constant.EnumUtils;
import com.yb.yff.game.data.constant.StaticConf;
import com.yb.yff.game.data.constant.myEnum.skill.EffectType;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.war.*;
import com.yb.yff.game.jsondb.data.dto.skill.Skill;

import com.yb.yff.game.data.constant.myEnum.skill.TargetType;
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
 * @Class: BattleLogic
 * @CreatedOn 2024/11/26.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗逻辑
 */
public class BattleLogic {
	private ArmyPositionLogic armyPositionLogic;
	private WarCampLogic warCampLogic;
	private JsonConfigMgr jsonConfigMgr;

	private ArmyMgrImpl armyMgr;

	public BattleLogic(JsonConfigMgr jsonConfigMgr, ArmyMgrImpl armyMgr, CityFacilityMgrImpl cityFacilityMgr,
	                   GeneralMgrImpl generalMgr){
		this.jsonConfigMgr = jsonConfigMgr;
		this.armyMgr = armyMgr;

		this.armyPositionLogic = new ArmyPositionLogic(jsonConfigMgr);
		this.warCampLogic = new WarCampLogic(cityFacilityMgr, armyMgr, generalMgr);
	}

	/**
	 * 战斗
	 * @param attack 攻方
	 * @param defense 守方
	 */
	public synchronized WarResultBO battle(WarArmyDTO attack, WarArmyDTO defense) {
		//初始化阵营数据
		WarCamp warCamp = new WarCamp(attack, defense);
		warCampLogic.initWarCamp(warCamp);

		WarResultBO result = new WarResultBO(warCamp);

		int curRound = 0;
		while (true) {
			WarRoundIsEndDTO round = runRound(result);
			result.getRounds().add(round);
			curRound++;
			if (curRound >= StaticConf.MAX_ROUND || round.getIsEnd()) {
				break;
			}
		}

		for (int i = 0; i < StaticConf.ARMY_G_CNT; i++) {
			if (result.getCamp().getAttackPos().get(i) != null) {
				result.getCamp().getAttack().getArmy().getSoldiers()
						.set(i, result.getCamp().getAttackPos().get(i).getSoldiers());
			}
			if (result.getCamp().getDefensePos().get(i) != null) {
				result.getCamp().getDefense().getArmy().getSoldiers()
						.set(i, result.getCamp().getDefensePos().get(i).getSoldiers());
			}
		}

		if (result.getCamp().getAttackPos().get(0) == null || result.getCamp().getAttackPos().get(0).getSoldiers() == 0) {
			result.setResult(0);
		} else if (result.getCamp().getDefensePos().get(0) != null && result.getCamp().getDefensePos().get(0).getSoldiers() != 0) {
			result.setResult(1);
		} else {
			result.setResult(2);
		}

		return result;
	}

	public List<SkillHitDTO> beforeSkill(ArmyPosition attacker, List<ArmyPosition> our, List<ArmyPosition> enemy) {
		List<AttachSkill> beforeSkills = armyPositionLogic.hitBefore(attacker);
		return acceptSkill(beforeSkills, attacker, our, enemy);
	}

	public List<SkillHitDTO> afterSkill(ArmyPosition armyPosition, List<ArmyPosition> our, List<ArmyPosition> enemy) {
		List<AttachSkill> afterSkills = armyPositionLogic.hitAfter(armyPosition);
		return acceptSkill(afterSkills, armyPosition, our, enemy);
	}

	public List<SkillHitDTO> acceptSkill(List<AttachSkill> skills, ArmyPosition att, List<ArmyPosition> our, List<ArmyPosition> enemy) {
		List<SkillHitDTO> ret = new ArrayList<>();
		for (AttachSkill aSkill : skills) {
			Skill cfg = aSkill.getCfg();
			SkillHitDTO sHit = new SkillHitDTO();
			sHit.setLv(aSkill.getLv());
			sHit.setCId(cfg.getCfgId());
			sHit.setTId(new ArrayList<>());
			sHit.setFId(att.getGeneral().getCfgId());
			sHit.setFIsA(att.isAttack()); // 0-攻城方，1-守城方
			sHit.setIE(cfg.getInclude_effect());
			sHit.setEV(cfg.getLevels().get(aSkill.getLv() - 1).getEffect_value());
			sHit.setER(cfg.getLevels().get(aSkill.getLv() - 1).getEffect_round());

			switch (EnumUtils.fromValue(TargetType.class, aSkill.getCfg().getTarget())) {
				case MySelf:
					aSkill.setEnemy(false);
					List<ArmyPosition> ps = List.of(att);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(att.isAttack());
					break;
				case OurSingle:
					aSkill.setEnemy(false);
					ArmyPosition s = warCampLogic.randArmyPosition(our);
					ps = List.of(s);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(att.isAttack());
					break;
				case OurMostTwo:
					aSkill.setEnemy(false);
					ps = warCampLogic.randMostTwoArmyPosition(our);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(att.isAttack());
					break;
				case OurMostThree:
					aSkill.setEnemy(false);
					ps = warCampLogic.randMostThreeArmyPosition(our);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(att.isAttack());
					break;
				case OurAll:
					aSkill.setEnemy(false);
					ps = warCampLogic.allArmyPosition(our);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(att.isAttack());
					break;
				case EnemySingle:
					aSkill.setEnemy(true);
					s = warCampLogic.randArmyPosition(enemy);
					ps = List.of(s);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(!att.isAttack());
					break;
				case EnemyMostTwo:
					aSkill.setEnemy(true);
					ps = warCampLogic.randMostTwoArmyPosition(enemy);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(!att.isAttack());
					break;
				case EnemyMostThree:
					aSkill.setEnemy(true);
					ps = warCampLogic.randMostThreeArmyPosition(enemy);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(!att.isAttack());
					break;
				case EnemyAll:
					aSkill.setEnemy(true);
					ps = warCampLogic.allArmyPosition(enemy);
					_acceptSkill_(ps, aSkill, sHit);
					sHit.setTIsA(!att.isAttack());
					break;
			}
			ret.add(sHit);
		}
		return ret;
	}

	private void _acceptSkill_(List<ArmyPosition> ps, AttachSkill skill, SkillHitDTO sh) {
		if (ps == null) {
			return;
		}
		for (ArmyPosition p : ps) {
			armyPositionLogic.acceptSkill(p, skill);
			sh.getTId().add(p.getGeneral().getCfgId());
		}
	}

	/**
	 * 战斗回合
	 * @param warResultBO
	 * @return
	 */
	public WarRoundIsEndDTO runRound(WarResultBO warResultBO) {
		if(warResultBO == null){
			return null;
		}

		WarRoundIsEndDTO curRound = new WarRoundIsEndDTO();
		warResultBO.setCurRound(curRound);

		List<ArmyPosition> attacks = warResultBO.getCamp().getAttackPos();
		List<ArmyPosition> defenses = warResultBO.getCamp().getDefensePos();

		// 随机先手
		Random random = new Random();
		int n = random.nextInt(10);
		if (n % 2 == 0) {
			attacks = warResultBO.getCamp().getDefensePos();
			defenses = warResultBO.getCamp().getAttackPos();
		}

		for (ArmyPosition hitA : attacks) {
			if (hitA == null || hitA.getSoldiers() == 0) {
				continue;
			}
			ArmyPosition hitB = warCampLogic.randArmyPosition(defenses);
			if (hitB == null) {
				curRound.setIsEnd(true);
				return curRound;
			}
			// A 打 B
			if (hit(warResultBO, hitA, hitB, attacks, defenses)) {
				curRound.setIsEnd(true);
				return curRound;
			}

			if (hitB.getSoldiers() == 0 || hitA.getSoldiers() == 0) {
				continue;
			}
			// B打A
			if (hit(warResultBO, hitB, hitA, defenses, attacks)) {
				curRound.setIsEnd(true);
				return curRound;
			}
		}

		//清理过期的技能功能效果
		for (ArmyPosition attack : attacks) {
			armyPositionLogic.checkNextRound(attack);
		}

		for (ArmyPosition defense : defenses) {
			armyPositionLogic.checkNextRound(defense);
		}

		curRound.setIsEnd(false);
		return curRound;
	}

	/**
	 * 攻击
	 * @param warResultBO
	 * @param attacker
	 * @param defender
	 * @param attacks
	 * @param defenses
	 * @return
	 */
	private boolean hit(WarResultBO warResultBO, ArmyPosition attacker, ArmyPosition defender, List<ArmyPosition> attacks, List<ArmyPosition> defenses) {
		HitDTO h = new HitDTO();
		h.setABs(beforeSkill(attacker, attacks, defenses));

		skillKill(attacker, defenses, h.getABs());

		if (defender.getSoldiers() > 0) {
			RealBattleAttr realA = armyPositionLogic.calRealBattleAttr(attacker);
			RealBattleAttr realB = armyPositionLogic.calRealBattleAttr(defender);
			int attKill = kill(attacker, defender, realA.getForce(), realB.getDefense());
			// NPC 军团没有过数据库，故没有ID,统一用cfgid
			h.setAId(attacker.getGeneral().getCfgId());
			h.setDId(defender.getGeneral().getCfgId());
			h.setDLoss(attKill);
		}

		armyPositionLogic.checkHit(attacker);
		armyPositionLogic.checkHit(defender);

		if (defender.getPosition() == 0 && defender.getSoldiers() == 0) {
			warResultBO.getCurRound().getB().add(h);
			return true;
		} else {
			h.setAAs(afterSkill(attacker, defenses, attacks));
			skillKill(attacker, defenses, h.getAAs());

			h.setDAs(afterSkill(defender, attacks, defenses));
			skillKill(defender, attacks, h.getDAs());

			warResultBO.getCurRound().getB().add(h);
			return false;
		}
	}

	private int kill(ArmyPosition hitA, ArmyPosition hitB, int aForce, int bDefense) {
		if (jsonConfigMgr.isDebug()) {
			int attKill = 10;
			hitB.setSoldiers(hitB.getSoldiers() - attKill);
			hitA.getGeneral().setExp(hitA.getGeneral().getExp() + attKill * 5);
			return attKill;
		} else {
			double attHarmRatio = armyMgr.getHarmRatio(hitA.getArms(), hitB.getArms());
			double attHarm = Math.abs(aForce - bDefense) * hitA.getSoldiers() * attHarmRatio * 0.0005;
			int attKill = (int) attHarm;
			attKill = Math.min(attKill, hitB.getSoldiers());
			hitB.setSoldiers(hitB.getSoldiers() - attKill);
			hitA.getGeneral().setExp(hitA.getGeneral().getExp() + attKill * 5);
			return attKill;
		}
	}

	private void skillKill(ArmyPosition hit, List<ArmyPosition> defenses, List<SkillHitDTO> skills) {
		for (SkillHitDTO skillHitDTO : skills) {
			skillHitDTO.setKill(new ArrayList<>());
			for (int i = 0; i < skillHitDTO.getIE().size(); i++) {
				if (skillHitDTO.getIE().get(i) == EffectType.HurtRate.getValue()) {
					int v = skillHitDTO.getEV().get(i);
					for (int j = 0; j < skillHitDTO.getTId().size(); j++) {
						int to = skillHitDTO.getTId().get(j);
						ArmyPosition hitTarget = warCampLogic.findByCfgId(defenses, to);
						if (hitTarget != null && hitTarget.getSoldiers() > 0) {
							RealBattleAttr realB = armyPositionLogic.calRealBattleAttr(hitTarget);
							RealBattleAttr realA = armyPositionLogic.calRealBattleAttr(hit);
							int force = realA.getForce() * v / 100;
							int attKill = kill(hit, hitTarget, force, realB.getDefense());
							skillHitDTO.getKill().add(attKill);
						}
					}
				}
			}
		}
	}

	public List<WarRoundIsEndDTO> getRounds(WarResultBO warResultBO) {
		return warResultBO.getRounds();
	}
}
