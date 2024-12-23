package com.yb.yff.game.business.businessLogic.impl.war;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.data.constant.EnumUtils;
import com.yb.yff.game.data.constant.myEnum.skill.EffectType;
import com.yb.yff.game.data.constant.myEnum.skill.TriggerType;
import com.yb.yff.game.data.dto.skill.SkillLevelDTO;
import com.yb.yff.game.data.dto.war.ArmyPosition;
import com.yb.yff.game.data.dto.war.AttachSkill;
import com.yb.yff.game.data.dto.war.RealBattleAttr;
import com.yb.yff.game.jsondb.data.dto.skill.Skill;
import com.yb.yff.game.jsondb.data.dto.skill.SkillLevels;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
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
 * @Class: ArmyPositionLogic
 * @CreatedOn 2024/11/27.
 * @Email: yangboyff@gmail.com
 * @Description: 战斗位置属性逻辑
 */
public class ArmyPositionLogic {
	JsonConfigMgr jsonConfigMgr;

	public ArmyPositionLogic(JsonConfigMgr jsonConfigMgr) {
		this.jsonConfigMgr = jsonConfigMgr;
	}

	//攻击前触发技能
	public List<AttachSkill> hitBefore(ArmyPosition attacker) {
		List<AttachSkill> ret = new ArrayList<>();

		for (SkillLevelDTO skill : attacker.getGeneral().getSkills()) {
			if (skill == null) {
				continue;
			}

			Skill skillCfg = jsonConfigMgr.getSkillConfig(skill.getCfgId());
			if (skillCfg == null) {
				continue;
			}

			// 只有 主动 和 指挥 技能是 前置触发
			if (skillCfg.getTrigger() != TriggerType.positive.getValue() &&
					skillCfg.getTrigger() != TriggerType.command.getValue()) {
				continue;
			}

			if (jsonConfigMgr.isDebug()) {
				AttachSkill as = new AttachSkill(skillCfg, skill.getId(), skill.getLv());
				ret.add(as);
			} else {
				SkillLevels l = skillCfg.getLevels().get(skill.getLv() - 1);
				Random random = new Random();
				int b = random.nextInt(100);
				if (b >= 100 - l.getProbability()) {
					AttachSkill as = new AttachSkill(skillCfg, skill.getCfgId(), skill.getLv());
					ret.add(as);
				}
			}
		}
		return ret;
	}

	//攻击后触发技能
	public List<AttachSkill> hitAfter(ArmyPosition armyPosition) {
		List<AttachSkill> ret = new ArrayList<>();

		for (SkillLevelDTO skill : armyPosition.getGeneral().getSkills()) {
			if (skill == null) {
				continue;
			}

			Skill skillCfg = jsonConfigMgr.getSkillConfig(skill.getCfgId());
			if (skillCfg != null) {
				if (skillCfg.getTrigger() != TriggerType.passive.getValue() &&
						skillCfg.getTrigger() != TriggerType.addAttack.getValue()) {
					continue;
				}
				if (jsonConfigMgr.isDebug()) {
					AttachSkill as = new AttachSkill(skillCfg, skill.getCfgId(), skill.getLv());
					ret.add(as);
				} else {
					SkillLevels l = skillCfg.getLevels().get(skill.getLv() - 1);
					Random random = new Random();
					int b = random.nextInt(100);
					if (b >= 100 - l.getProbability()) {
						AttachSkill as = new AttachSkill(skillCfg, skill.getCfgId(), skill.getLv());
						ret.add(as);
					}
				}
			}
		}
		return ret;
	}

	public void acceptSkill(ArmyPosition armyPosition, AttachSkill skill) {
		if (armyPosition.getSkills() == null) {
			armyPosition.setSkills(new ArrayList<>());
		}
		armyPosition.getSkills().add(skill);
	}

	public void checkHit(ArmyPosition armyPosition) {
		if (armyPosition.getSkills() == null) {
			return;
		}

		Iterator<AttachSkill> iterator = armyPosition.getSkills().iterator();
		while (iterator.hasNext()) {
			AttachSkill skill = iterator.next();
			if (skill.getDuration() <= 0) {
				// 瞬时技能，当前攻击完成后移除
				iterator.remove();
			}
		}
	}

	public void checkNextRound(ArmyPosition armyPosition) {
		if (armyPosition == null || armyPosition.getSkills() == null) {
			return;
		}

		Iterator<AttachSkill> iterator = armyPosition.getSkills().iterator();
		while (iterator.hasNext()) {
			AttachSkill skill = iterator.next();
			skill.setDuration(skill.getDuration() - 1);
			if (skill.getDuration() > 0) {
				//持续技能，当前回合结束后持续到期移除
				iterator.remove();
			}
		}
	}

	//计算真正的战斗属性，包含了技能
	public RealBattleAttr calRealBattleAttr(ArmyPosition armyPosition) {
		RealBattleAttr attr = new RealBattleAttr();
		BeanUtils.copyProperties(armyPosition, attr);

		if (armyPosition.getSkills() == null) {
			return attr;
		}

		for (AttachSkill skill : armyPosition.getSkills()) {

			Skill skillCfg = jsonConfigMgr.getSkillConfig(skill.getCfg().getCfgId());

			SkillLevels lvData = skillCfg.getLevels().get(skill.getLv() - 1);
			List<Integer> effects = skillCfg.getInclude_effect();

			for (int i = 0; i < effects.size(); i++) {
				int effect = effects.get(i);
				int v = lvData.getEffect_value().get(i);
				switch (EnumUtils.fromValue(EffectType.class, effect)) {
					case HurtRate:
						break;
					case Force:
						attr.setForce(attr.getForce() + v);
						break;
					case Defense:
						attr.setDefense(attr.getDefense() + v);
						break;
					case Strategy:
						attr.setStrategy(attr.getStrategy() + v);
						break;
					case Speed:
						attr.setSpeed(attr.getSpeed() + v);
						break;
					case Destroy:
						attr.setDestroy(attr.getDestroy() + v);
						break;
				}
			}
		}
		return attr;
	}
}
