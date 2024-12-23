package com.yb.yff.game.business.businessLogic.impl.war;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.*;
import com.yb.yff.game.business.businessLogic.IArmyLogic;
import com.yb.yff.game.business.businessLogic.IWarReportLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.business.businessLogic.impl.map.MapPositionLogic;
import com.yb.yff.game.data.bo.WarResultBO;
import com.yb.yff.game.data.constant.myEnum.ArmyCmd;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.dto.nationMap.MapBuildDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.war.WarReportAndWarResultDTO;
import com.yb.yff.game.utils.CityPositionUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
 * @Class: ArmyWarLogic
 * @CreatedOn 2024/11/24.
 * @Email: yangboyff@gmail.com
 * @Description: 军团战斗逻辑
 */

@Service
@Slf4j
public class ArmyWarLogic {
	final CityMgrImpl cityMgr;

	final BuildMgrImpl buildMgr;

	final ArmyMgrImpl armyMgr;

	final GeneralMgrImpl generalMgr;

	final MapPositionLogic mapPositionLogic;

	final CityFacilityMgrImpl cityFacilityMgr;

	final IArmyLogic armyLogic;

	final JsonConfigMgr jsonConfigMgr;

	final WarReportMgrImpl warReportMgr;

	final IWarReportLogic warReportLogic;

	final RoleDataMgrImpl roleDataManager;

	final BusinessDataSyncImpl<MapBuildDTO> nationMapPusher;

	final BusinessDataSyncImpl<CityDTO> cityDataPusher;

	final BusinessDataSyncImpl<GeneralDTO> generalDataPusher;

	final BusinessDataSyncImpl<WarReportDTO> warReportDataPusher;


	// 战斗逻辑
	BattleLogic battleLogic;

	@Autowired
	public ArmyWarLogic(IWarReportLogic warReportLogic, WarReportMgrImpl warReportMgr,
	                    BusinessDataSyncImpl<WarReportDTO> warReportDataPusher, ArmyMgrImpl armyMgr,
	                    JsonConfigMgr jsonConfigMgr, RoleDataMgrImpl roleDataManager, IArmyLogic armyLogic,
	                    CityMgrImpl cityMgr, CityFacilityMgrImpl cityFacilityMgr, MapPositionLogic mapPositionLogic,
	                    BusinessDataSyncImpl<CityDTO> cityDataPusher, BuildMgrImpl buildMgr,
	                    BusinessDataSyncImpl<MapBuildDTO> nationMapPusher,
	                    GeneralMgrImpl generalMgr, BusinessDataSyncImpl<GeneralDTO> generalDataPusher) {
		this.warReportLogic = warReportLogic;
		this.warReportMgr = warReportMgr;
		this.warReportDataPusher = warReportDataPusher;
		this.jsonConfigMgr = jsonConfigMgr;
		this.roleDataManager = roleDataManager;
		this.cityMgr = cityMgr;
		this.cityFacilityMgr = cityFacilityMgr;
		this.cityDataPusher = cityDataPusher;
		this.buildMgr = buildMgr;
		this.mapPositionLogic = mapPositionLogic;
		this.nationMapPusher = nationMapPusher;
		this.armyMgr = armyMgr;
		this.armyLogic = armyLogic;
		this.generalMgr = generalMgr;
		this.generalDataPusher = generalDataPusher;
	}

	@PostConstruct
	public void init() {
		battleLogic = new BattleLogic(jsonConfigMgr, armyMgr, cityFacilityMgr, generalMgr);
	}

	public void newBattle(WarArmyDTO attackArmy) {
		Integer toX = attackArmy.getArmy().getTo_x();
		Integer toY = attackArmy.getArmy().getTo_y();

		CityDTO city = cityMgr.getPositionCity(toX, toY);

		// 攻打逻辑
		if (city != null) {
			// city
			attackCity(attackArmy, city);
		} else {
			// build
			attackBuild(attackArmy);
		}
	}

	private void attackCity(WarArmyDTO attackArmy, CityDTO city) {
		// 统计 City 军团：  驻守队伍
		List<WarArmyDTO> enemies = armyMgr.getStopInPosArmyListWar(city.getX(), city.getY());

		// 统计 City 军团：  城内空闲的队伍
		List<WarArmyDTO> cityArmies = armyMgr.getCityArmyWar(city.getCityId());
		for (WarArmyDTO army : cityArmies) {
			if (army.getArmy().getGenerals().get(0) != 0 &&
					(army.getArmy().getCmd() == ArmyCmd.ArmyCmdIdle.getValue())) {
				enemies.add(army);
			}
		}

		if (enemies.isEmpty()) {
			// 没有队伍
			int destroy = armyMgr.getDestroy(attackArmy.getArmy());
			cityMgr.durableChange(city, -destroy);

			cityDataPusher.syncExecute(city.getRid(), city);

			WarReportDTO wr = newEmptyWar(attackArmy);
			wr.setResult(2);
			wr.setD_rid(city.getRid());
			wr.setD_is_read(false);
			checkCityOccupy(wr, attackArmy, city);

			warReportDataPusher.syncExecute(null, wr);
		} else {
			List<WarReportDTO> warReports = new ArrayList<>();

			WarResultBO lastWar = createWar(attackArmy, enemies, true, warReports);

			if (lastWar.getResult() > 1) {
				WarReportDTO wr = warReports.get(warReports.size() - 1);
				checkCityOccupy(wr, attackArmy, city);
			}

			warReports.forEach(wr -> warReportDataPusher.syncExecute(null, wr));
		}
	}

	private void checkCityOccupy(WarReportDTO wr, WarArmyDTO attackArmy, CityDTO city) {
		int destroy = armyMgr.getDestroy(attackArmy.getArmy());

		wr.setDestroy(Math.min(destroy, city.getCur_durable()));
		cityMgr.durableChange(city, -destroy);

		if (city.getCur_durable() == 0) {
			RoleDTO role = roleDataManager.getRoleDTO(attackArmy.getArmy().getRid());
			Integer allianceId = role.getUnionId();
			if (allianceId != null && allianceId != 0) {
				// 有联盟才能俘虏玩家
				wr.setOccupy(1);
				RoleDTO dRole = roleDataManager.getRoleDTO(city.getRid());
				dRole.setParentId(allianceId);
//				Union.getInstance().putChild(allianceId, city.getRid());
//				dRole.syncExecute();
				city.setOccupy_time(new Date());
			} else {
				wr.setOccupy(0);
			}
		} else {
			wr.setOccupy(0);
		}

		cityDataPusher.syncExecute(city.getRid(), city);
	}

	/**
	 * 战斗
	 *
	 * @param army
	 * @param enemies
	 * @param isRoleEnemy enemies 是：玩家军团， 否 ：NPC军团
	 * @param warReports
	 * @return
	 */
	private WarResultBO createWar(WarArmyDTO army, List<WarArmyDTO> enemies, boolean isRoleEnemy, List<WarReportDTO> warReports) {
		AtomicReference<WarResultBO> lastWar = new AtomicReference<>();

		int posId = CityPositionUtils.position2Number(army.getArmy().getTo_x(), army.getArmy().getTo_y());

		enemies.forEach(enemy -> {
			// 战斗
			WarReportAndWarResultDTO warReportAndWarResultDTO = triggerBattle(army, enemy);

			WarResultBO warResultBO = warReportAndWarResultDTO.getWarResultBO();

			WarReportDTO wr = warReportAndWarResultDTO.getWarReport();

			warReports.add(wr);

			if (isRoleEnemy) {
				if (warResultBO.getResult() > 1) {

					armyMgr.deleteStopInPosNPCArmys(posId);

					armyLogic.armyBack(enemy);
				}

				armyLogic.checkSyncCell(enemy);
			} else {
				// NPC 直接已读
				wr.setD_is_read(true);
			}

			lastWar.set(warResultBO);
		});

		armyLogic.checkSyncCell(army);

		return lastWar.get();
	}

	/**
	 * 触发战斗
	 *
	 * @param army
	 * @param enemy
	 * @return
	 */
	public WarReportAndWarResultDTO triggerBattle(WarArmyDTO army, WarArmyDTO enemy) {
		WarReportAndWarResultDTO warReportAndWarResultDTO = new WarReportAndWarResultDTO();

		/*** 战前数据 ***/
		// 军团
		String begArmy1Str = JSONObject.toJSONString(army.getArmy());
		String begArmy2Set = JSONObject.toJSONString(enemy.getArmy());

		// 攻方
		List<List<Integer>> begGeneral1 = new ArrayList<>();
		for (GeneralDTO general : army.getGeneralList()) {
			if (general != null) {
				begGeneral1.add(generalMgr.getGeneralValue(general));
			}
		}
		String begGeneral1Str = JSONArray.toJSONString(begGeneral1);

		// 守方
		List<List<Integer>> begGeneral2 = new ArrayList<>();
		for (GeneralDTO general : enemy.getGeneralList()) {
			if (general != null) {
				begGeneral2.add(generalMgr.getGeneralValue(general));
			}
		}
		String begGeneral2Str = JSONArray.toJSONString(begGeneral2);


		/*** 战斗 ***/
		WarResultBO warResultBO = battleLogic.battle(army, enemy);
		warReportAndWarResultDTO.setWarResultBO(warResultBO);


		/*** 战后数据 ***/
		// 攻方
		List<List<Integer>> endGeneral1 = new ArrayList<>();
		for (GeneralDTO g : army.getGeneralList()) {
			if (g != null) {
				endGeneral1.add(generalMgr.getGeneralValue(g));

				// 战斗经验处理
				generalMgr.expToLevel(g);
				generalDataPusher.syncExecute(g.getRid(), g);

			}
		}
		String endGeneral1Str = JSONArray.toJSONString(endGeneral1);

		// 守方
		List<List<Integer>> endGeneral2 = new ArrayList<>();
		for (GeneralDTO g : enemy.getGeneralList()) {
			if (g != null) {
				endGeneral2.add(generalMgr.getGeneralValue(g));

				// 战斗经验处理
				generalMgr.expToLevel(g);
				generalDataPusher.syncExecute(g.getRid(), g);
			}
		}
		String endGeneral2Str = JSONArray.toJSONString(endGeneral2);

		// 战后数据
		String endArmy1Str = JSONObject.toJSONString(army.getArmy());
		String endArmy2Set = JSON.toJSONString(enemy.getArmy());

		String rounds = JSONArray.toJSONString(warResultBO.getWarRounds());
		WarReportDTO wr = new WarReportDTO();
		wr.setX(army.getArmy().getTo_x());
		wr.setY(army.getArmy().getTo_y());
		wr.setA_rid(army.getArmy().getRid());
		wr.setA_is_read(false);
		wr.setD_is_read(false);
		wr.setD_rid(enemy.getArmy().getRid());
		wr.setB_a_army(begArmy1Str);
		wr.setB_d_army(begArmy2Set);
		wr.setE_a_army(endArmy1Str);
		wr.setE_d_army(endArmy2Set);
		wr.setB_a_general(begGeneral1Str);
		wr.setB_d_general(begGeneral2Str);
		wr.setE_a_general(endGeneral1Str);
		wr.setE_d_general(endGeneral2Str);
		wr.setRounds(rounds);
		wr.setResult(warResultBO.getResult());
		wr.setCtime(System.currentTimeMillis() / 1000);

		warReportAndWarResultDTO.setWarReport(wr);

		return warReportAndWarResultDTO;
	}

	public WarReportDTO newEmptyWar(WarArmyDTO attackArmy) {
		// 战报处理
		List<List<Integer>> begGenerals = new ArrayList<>();
		for (GeneralDTO general : attackArmy.getGeneralList()) {
			if (general != null) {
				begGenerals.add(generalMgr.getGeneralValue(general));
			}
		}
		String bgGeneralStr = JSONArray.toJSONString(begGenerals);

		String attackArmyStr = JSON.toJSONString(attackArmy.getArmy());

		WarReportDTO wr = new WarReportDTO();
		wr.setX(attackArmy.getArmy().getTo_x());
		wr.setY(attackArmy.getArmy().getTo_y());
		wr.setA_rid(attackArmy.getArmy().getRid());
		wr.setA_is_read(false);
		wr.setD_is_read(true);
		wr.setD_rid(0);
		wr.setB_a_army(attackArmyStr);
		wr.setB_d_army(null);
		wr.setE_a_army(attackArmyStr);
		wr.setE_d_army(null);
		wr.setB_a_general(bgGeneralStr);
		wr.setE_a_general(bgGeneralStr);
		wr.setB_d_general(null);
		wr.setE_d_general(null);
		wr.setRounds("");
		wr.setResult(0);
		wr.setCtime(System.currentTimeMillis() / 1000);

		return wr;
	}


	private void attackBuild(WarArmyDTO army) {
		Integer toX = army.getArmy().getTo_x();
		Integer toY = army.getArmy().getTo_y();

		boolean isRoleEnemy = false;
		List<WarArmyDTO> enemies = armyMgr.getStopInPosArmyListWar(toX, toY);
		if (enemies != null && enemies.size() > 0) {
			isRoleEnemy = true;
		} else {
			isRoleEnemy = false;
			enemies = armyLogic.getNpcArmyListByPos(toX, toY);
			if (enemies == null || enemies.size() == 0) {
				log.error("NPC 军团 配置 异常");
			}
		}

		MapBuildDTO roleBuild = buildMgr.getPositionBuild(toX, toY);

		List<WarReportDTO> warReports = new ArrayList<>();
		WarResultBO lastWar = createWar(army, enemies, isRoleEnemy, warReports);

		if (lastWar.getResult() > 1) {
			if (roleBuild != null && roleBuild.getRid() > 0) {
				int destroy = armyMgr.getDestroy(army.getArmy());
				WarReportDTO wr = warReports.get(warReports.size() - 1);
				wr.setDestroy(Math.min(destroy, roleBuild.getCur_durable()));
				roleBuild.setCur_durable(Math.max(0, roleBuild.getCur_durable() - destroy));
				if (roleBuild.getCur_durable() == 0) {
					// 攻占了玩家的领地
					int bLimit = roleDataManager.getRoleConfig().getBuild_limit();
					if (bLimit > buildMgr.buildCnt(army.getArmy().getRid())) {
						wr.setOccupy(1);
						buildMgr.removeBuildFromRole(roleBuild.getX(), roleBuild.getY());
						nationMapPusher.syncExecute(roleBuild.getRid(), roleBuild);

						buildMgr.addBuild2Role(army.getArmy().getRid(), toX, toY);
						occupyRoleBuild(army.getArmy().getRid(), toX, toY);
					} else {
						wr.setOccupy(0);
					}
				} else {
					wr.setOccupy(0);
				}
			} else {
				// 占领系统领地
				WarReportDTO wr = warReports.get(warReports.size() - 1);
				int bLimit = roleDataManager.getRoleConfig().getBuild_limit();
				if (bLimit > buildMgr.buildCnt(army.getArmy().getRid())) {
					occupySystemBuild(army.getArmy().getRid(), toX, toY);
					wr.setDestroy(10000);
					wr.setOccupy(1);
				} else {
					wr.setOccupy(0);
				}
				armyLogic.removeNPCArmy(toX, toY);
			}
		}

		// 领地发生变化
		MapBuildDTO newRoleBuild = buildMgr.getPositionBuild(toX, toY);
		if (newRoleBuild != null) {
			nationMapPusher.syncExecute(newRoleBuild.getRid(), newRoleBuild);
		}

		warReports.forEach(wr -> warReportDataPusher.syncExecute(null, wr));
	}

	private void occupyRoleBuild(int rid, Integer toX, Integer toY) {
		int newId = rid;

		MapBuildDTO build = buildMgr.getPositionBuild(toX, toY);
		if (build != null) {
			build.setCur_durable(build.getMax_durable());
			build.setOccupy_time(System.currentTimeMillis());

			int oldId = build.getRid();
			log.info("hit in role build", "oldRId", oldId, "newRId", newId);
			build.setRid(rid);
		}
	}

	private void occupySystemBuild(int rid, Integer toX, Integer toY) {
		MapBuildDTO r = buildMgr.getPositionBuild(toX, toY);
		if (r != null && r.getRid() > 0) {
			return;
		}

		if (buildMgr.isCanBuild(toX, toY)) {
			buildMgr.addBuild2Role(rid, toX, toY);
		}
	}
}
