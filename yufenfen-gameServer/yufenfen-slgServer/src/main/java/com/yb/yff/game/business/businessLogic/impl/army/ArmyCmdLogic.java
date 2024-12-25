package com.yb.yff.game.business.businessLogic.impl.army;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.ArmyMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.BuildMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.CityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessLogic.IArmyLogic;
import com.yb.yff.game.business.businessLogic.INationMapLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.business.businessLogic.impl.map.MapPositionLogic;
import com.yb.yff.game.business.businessLogic.impl.war.ArmyWarLogic;
import com.yb.yff.game.data.constant.EnumUtils;
import com.yb.yff.game.data.constant.myEnum.ArmyCmd;
import com.yb.yff.game.data.constant.myEnum.ArmyState;
import com.yb.yff.game.data.constant.myEnum.BuildType;
import com.yb.yff.game.data.dto.army.ArmyDTO;
import com.yb.yff.game.data.dto.army.AssignDTO;
import com.yb.yff.game.data.dto.army.WarArmyDTO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.nationMap.MapBuildDTO;
import com.yb.yff.game.data.dto.role.RoleResourceData;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.game.utils.PositionUtils;
import com.yb.yff.sb.config.AsyncThreadPoolConfig;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

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
 * @Class: ArmyCmdLogic
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 军团指令逻辑处理
 */
@Slf4j
class ArmyCmdLogic {
	private final ArmyMgrImpl armyMgr;
	private final BuildMgrImpl buildMgr;
	private final CityMgrImpl cityMgr;
	private final MapPositionLogic mapPositionLogic;
	private final RoleDataMgrImpl roleDataManager;
	private final ArmyWarLogic armyWarLogic;
	private final IArmyLogic armyLogic;
	private final INationMapLogic nationMapLogic;
	private final JsonConfigMgr jsonConfigMgr;
	private final BusinessDataSyncImpl<WarReportDTO> warReportDataPusher;
	private final BusinessDataSyncImpl<RoleResourceData> roleDataPusher;


	public ArmyCmdLogic(JsonConfigMgr jsonConfigMgr, RoleDataMgrImpl roleDataManager,
	                    BusinessDataSyncImpl<RoleResourceData> roleDataPusher,
	                    BusinessDataSyncImpl<WarReportDTO> warReportDataPusher,
	                    BuildMgrImpl buildMgr, CityMgrImpl cityMgr, IArmyLogic armyLogic,
	                    MapPositionLogic mapPositionLogic, INationMapLogic nationMapLogic,
	                    ArmyMgrImpl armyMgr, ArmyWarLogic armyWarLogic) {
		this.warReportDataPusher = warReportDataPusher;
		this.roleDataPusher = roleDataPusher;
		this.armyMgr = armyMgr;
		this.buildMgr = buildMgr;
		this.cityMgr = cityMgr;
		this.roleDataManager = roleDataManager;
		this.mapPositionLogic = mapPositionLogic;
		this.armyLogic = armyLogic;
		this.nationMapLogic = nationMapLogic;

		this.jsonConfigMgr = jsonConfigMgr;

		this.armyWarLogic = armyWarLogic;

		init();
	}

	/**
	 * 处理军团指令
	 *
	 * @param army
	 * @param assignDTO
	 * @return
	 */
	public ResponseCode processingCommands(WarArmyDTO army, AssignDTO assignDTO) {
		switch (EnumUtils.fromValue(ArmyCmd.class, assignDTO.getCmd())) {
			case ArmyCmdBack:
				ResponseCode ret = fromCurrentCmd2Retreat(army);
				if (ret != NetResponseCodeConstants.SUCCESS) {
					return ret;
				}
				break;
			case ArmyCmdAttack:
				ResponseCode retAttack = fromCurrentCmd2Attack(army, assignDTO);
				if (retAttack != NetResponseCodeConstants.SUCCESS) {
					return retAttack;
				}
				break;
			case ArmyCmdDefend:
				ResponseCode retStay = fromCurrentCmd2Stay(army, assignDTO);
				if (retStay != NetResponseCodeConstants.SUCCESS) {
					return retStay;
				}
				break;
			case ArmyCmdReclamation:
				ResponseCode retReclaim = fromCurrentCmd2Reclaim(army, assignDTO);
				if (retReclaim != NetResponseCodeConstants.SUCCESS) {
					return retReclaim;
				}
				break;
			case ArmyCmdTransfer:
				ResponseCode retTransfer = fromCurrentCmd2Transfer(army, assignDTO);
				if (retTransfer != NetResponseCodeConstants.SUCCESS) {
					return retTransfer;
				}
				break;
			default:
				break;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 根据当前执行的指令状态，转向撤退指令
	 *
	 * @param warArmy
	 * @return
	 */
	private ResponseCode fromCurrentCmd2Retreat(WarArmyDTO warArmy) {
		ArmyDTO army = warArmy.getArmy();
		if (army.getCmd().equals(ArmyCmd.ArmyCmdAttack.getValue()) ||
				army.getCmd().equals(ArmyCmd.ArmyCmdDefend.getValue()) ||
				army.getCmd().equals(ArmyCmd.ArmyCmdReclamation.getValue())) {
			armyRetreat(warArmy);
		} else if (army.getCmd().equals(ArmyCmd.ArmyCmdIdle.getValue())) {
			CityDTO city = cityMgr.getCity(army.getCityId());

			if (PositionUtils.equalsPosition(army.getFromX(), army.getFromY(), city.getX(), city.getY())) {
				armyRetreat(warArmy);
			}
		}

		return NetResponseCodeConstants.SUCCESS;
	}


	/**
	 * 根据当前执行的指令状态，转向攻击指令
	 *
	 * @param warArmy
	 * @return
	 */
	private ResponseCode fromCurrentCmd2Attack(WarArmyDTO warArmy, AssignDTO assignDTO) {
		ArmyDTO army = warArmy.getArmy();

		ResponseCode code = commandPre(warArmy, assignDTO);
		if (code != NetResponseCodeConstants.SUCCESS) {
			return code;
		}

		//免战
		if (mapPositionLogic.isWarFree(assignDTO.getX(), assignDTO.getY())) {
			return NetResponseCodeConstants.BuildWarFree;
		}

		if (mapPositionLogic.isCanDefend(army.getRid(), assignDTO.getX(), assignDTO.getY())) {
			return NetResponseCodeConstants.BuildCanNotAttack;
		}

		return commandAfter(warArmy, assignDTO);
	}


	/**
	 * 根据当前执行的指令状态，转向驻守指令
	 *
	 * @param army
	 * @return
	 */
	private ResponseCode fromCurrentCmd2Stay(WarArmyDTO army, AssignDTO assignDTO) {
		ResponseCode code = commandPre(army, assignDTO);
		if (code != NetResponseCodeConstants.SUCCESS) {
			return code;
		}

		if (!mapPositionLogic.isCanDefend(army.getArmy().getRid(), assignDTO.getX(), assignDTO.getY())) {
			return NetResponseCodeConstants.BuildCanNotDefend;
		}

		return commandAfter(army, assignDTO);
	}

	/**
	 * 根据当前执行的指令状态，转向屯垦指令
	 *
	 * @param army
	 * @return
	 */
	private ResponseCode fromCurrentCmd2Reclaim(WarArmyDTO army, AssignDTO assignDTO) {
		ResponseCode code = commandPre(army, assignDTO);
		if (code != NetResponseCodeConstants.SUCCESS) {
			return code;
		}

		MapBuildDTO build = buildMgr.getPositionBuild(assignDTO.getX(), assignDTO.getY());
		if (build.getRid() != army.getArmy().getRid()) {
			return NetResponseCodeConstants.BuildNotMe;
		}

		return commandAfter(army, assignDTO);
	}

	/**
	 * 根据当前执行的指令状态，转向调动指令
	 *
	 * @param warArmy
	 * @param assignDTO
	 * @return
	 */
	private ResponseCode fromCurrentCmd2Transfer(WarArmyDTO warArmy, AssignDTO assignDTO) {
		ArmyDTO army = warArmy.getArmy();

		ResponseCode code = commandPre(warArmy, assignDTO);
		if (code != NetResponseCodeConstants.SUCCESS) {
			return code;
		}

		if (PositionUtils.equalsPosition(army.getFromX(), army.getFromY(), assignDTO.getX(), assignDTO.getY())) {
			return NetResponseCodeConstants.CanNotTransfer;
		}

		MapBuildDTO build = buildMgr.getPositionBuild(assignDTO.getX(), assignDTO.getY());
		if (build == null || build.getRid() != army.getRid()) {
			return NetResponseCodeConstants.BuildNotMe;
		}

		if (build.getLevel() <= 0 || !buildMgr.isHasTransferAuth(build)) {
			return NetResponseCodeConstants.CanNotTransfer;
		}

		int cnt = 0;
		if (build.getType().equals(BuildType.MapBuildFortress.getValue())) {
			Integer cntObj = jsonConfigMgr.getNationArmyCntConfig(build.getType(), build.getLevel());
			if(cntObj == null){
				return NetResponseCodeConstants.InvalidParam;
			}
			cnt = cntObj;
		} else {
			cnt = 5;
		}


		if (armyMgr.belongPosArmyCnt(build.getRid(), new PositionDTO(assignDTO.getX(), assignDTO.getY())) > cnt) {
			return NetResponseCodeConstants.HoldIsFull;
		}

		return commandAfter(warArmy, assignDTO);
	}


	/**
	 * 军队撤退
	 *
	 * @param army
	 */
	private void armyRetreat(WarArmyDTO army) {
		clearConscript(army.getArmy());

		army.getArmy().setState(ArmyState.ArmyRunning.getValue());
		army.getArmy().setCmd(ArmyCmd.ArmyCmdBack.getValue());

		synchronized (this) {
			Long endTime = army.getArmy().getEnd() / 1000;
			List<WarArmyDTO> endTimeArmys = armyMgr.getEndTimeArmy(endTime.intValue());
			for (WarArmyDTO endArmy : endTimeArmys) {
				if (army.getArmy().getId().equals(endArmy.getArmy().getId())) {
					endTimeArmys.remove(endArmy);
					break;
				}
			}
		}

		pushAction(army);
	}

	/**
	 * 清除征兵状态
	 *
	 * @param army
	 */
	private void clearConscript(ArmyDTO army) {
		if (army.getCmd().equals(ArmyCmd.ArmyCmdConscript.getValue())) {
			List<Integer> initVals = List.of(0, 0, 0);
			army.setConCnts(initVals);
			army.setConTimes(initVals);
			army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
		}
	}

	/**
	 * 命令预处理
	 *
	 * @param army
	 * @param assignDTO
	 * @return
	 */
	private ResponseCode commandPre(WarArmyDTO army, AssignDTO assignDTO) {
		if (assignDTO.getX() < 0 || assignDTO.getX() >= CityPositionUtils.MapWidth ||
				assignDTO.getY() < 0 || assignDTO.getY() >= CityPositionUtils.MapHeight) {
			return NetResponseCodeConstants.InvalidParam;
		}

		if (!armyMgr.canGoTOWar(army.getArmy())) {
			if (army.getArmy().getCmd() != ArmyCmd.ArmyCmdIdle.getValue()) {
				return NetResponseCodeConstants.ArmyBusy;
			} else {
				return NetResponseCodeConstants.ArmyNotMain;
			}
		}

		if (army.getArmy().getCmd() != ArmyCmd.ArmyCmdIdle.getValue()) {
			return NetResponseCodeConstants.ArmyBusy;
		}

		//判断该地是否是能攻击类型
		MapBuildDTO mapBuildDTO = buildMgr.getPositionBuild(assignDTO.getY(), assignDTO.getY());
		if (mapBuildDTO == null || mapBuildDTO.getType().equals(0)) {
			return NetResponseCodeConstants.InvalidParam;
		}

		if (!mapPositionLogic.isCanArrive(army.getArmy().getRid(), assignDTO.getY(), assignDTO.getY())) {
			return NetResponseCodeConstants.UnReachable;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 命令后处理
	 *
	 * @param warArmy
	 * @param assignDTO
	 * @return
	 */
	private ResponseCode commandAfter(WarArmyDTO warArmy, AssignDTO assignDTO) {
		ArmyDTO army = warArmy.getArmy();

		//最后才消耗体力
		Integer cost = jsonConfigMgr.getBasicConfig().getGeneral().getCost_physical_power();

		if (!armyMgr.physicalPowerIsEnough(army, cost)) {
			return NetResponseCodeConstants.PhysicalPowerNotEnough;
		}

		if (army.getCmd().equals(ArmyCmd.ArmyCmdReclamation.getValue()) || army.getCmd().equals(ArmyCmd.ArmyCmdTransfer.getValue())) {
			Integer configCost = jsonConfigMgr.getBasicConfig().getGeneral().getReclamation_cost();
			if (!roleDataManager.decreeIsEnough(army.getRid(), configCost)) {
				return NetResponseCodeConstants.DecreeNotEnough;
			} else {
				roleDataManager.tryUseDecree(army.getRid(), configCost);
			}
		}

		armyMgr.tryUsePhysicalPower(army, cost);

		army.setTo(assignDTO.getX(), assignDTO.getY());

		army.setCmd(assignDTO.getCmd());
		army.setState(ArmyState.ArmyRunning.getValue());

		if (jsonConfigMgr.isDebug()) {
			army.setStart(System.currentTimeMillis() / 1000);
			army.setEnd(System.currentTimeMillis() / 1000 + 40);
		} else {
			Integer speed = armyMgr.countSpeed(army);
			Integer t = mapPositionLogic.travelTime(speed, army.getFromX(), army.getFromY(), assignDTO.getX(), assignDTO.getY());
			army.setStart(System.currentTimeMillis() / 1000);
			army.setEnd(System.currentTimeMillis() / 1000 + t);
		}

		pushAction(warArmy);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * @param warArmy
	 */
	public void pushAction(WarArmyDTO warArmy) {
		ArmyDTO army = warArmy.getArmy();
		switch (EnumUtils.fromValue(ArmyCmd.class, army.getCmd())) {
			case ArmyCmdAttack:
			case ArmyCmdDefend:
			case ArmyCmdTransfer:
				addAction(warArmy);
				break;
			case ArmyCmdReclamation:
				if (army.getState().equals(ArmyState.ArmyRunning.getValue())) {
					addAction(warArmy);
				} else {
					Integer costTime = jsonConfigMgr.getBasicConfig().getGeneral().getReclamation_cost();
					Integer tendTime = army.getEnd().intValue() + costTime;

					addAction(tendTime, warArmy);
				}
				break;
			case ArmyCmdBack:

				if (PositionUtils.equalsPosition(army.getFromX(), army.getFromY(), army.getToX(), army.getToY())) {
					//处理调动到其他地方待命的情况，回归属的城池
					CityDTO city = cityMgr.getCity(army.getCityId());
					if (city != null) {
						army.setFrom(city.getX(), city.getY());

						//计算回去的时间
						if (jsonConfigMgr.isDebug()) {
							army.setStart(System.currentTimeMillis() / 1000);
							army.setEnd(System.currentTimeMillis() / 1000 + 40);
						} else {
							Integer speed = armyMgr.countSpeed(army);
//						PositionDTO from = new PositionDTO(army.getFrom_x(), army.getFrom_y());
//						PositionDTO to = new PositionDTO(army.getTo_x(), army.getTo_y());
							Integer t = mapPositionLogic.travelTime(speed, army.getFromX(), army.getFromY(), army.getToX(), army.getToY());
							army.setStart(System.currentTimeMillis() / 1000);
							army.setEnd(System.currentTimeMillis() / 1000 + t);
						}
					}

				} else {
					Long cur = System.currentTimeMillis() / 1000;
					Long diff = army.getEnd() - army.getStart();
					if (cur < army.getEnd()) {
						diff = cur - army.getStart();
					}
					army.setStart(cur);
					army.setEnd(cur + diff);

				}
				army.setCmd(ArmyCmd.ArmyCmdBack.getValue());
				this.addAction(warArmy);
				break;
			default:
				break;
		}

		updateArmys(warArmy);
	}

	private void addAction(WarArmyDTO army) {
		Integer endTime = army.getArmy().getEnd().intValue();

		addAction(endTime, army);
	}

	private void addAction(Integer endTime, WarArmyDTO army) {
		armyMgr.addEndTimeArmy(endTime, army);
	}


	private void init() {
		Map<Integer, WarArmyDTO> armyMap = armyMgr.getArmyAll();
		for (Map.Entry<Integer, WarArmyDTO> entry : armyMap.entrySet()) {
			WarArmyDTO warArmyDTO = entry.getValue();
			ArmyDTO army = warArmyDTO.getArmy();
			//恢复已经执行行动的军队
			if (!army.getCmd().equals(ArmyCmd.ArmyCmdIdle.getValue())) {
				if (army.getEnd() == null || army.getEnd() == 0 || army.getStart() == null || army.getStart() == 0) {
					army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
					continue;
				}
				Integer e = army.getEnd().intValue();

				armyMgr.addEndTimeArmy(e, warArmyDTO);
			}
		}
	}

	private synchronized void updateArmys(WarArmyDTO warArmy) {
		ArmyDTO army = warArmy.getArmy();
		armyLogic.checkSyncCell(warArmy);
		if (army.getCmd().equals(ArmyCmd.ArmyCmdBack.getValue())) {
			List<WarArmyDTO> posArmys = armyMgr.getStopInPosArmyMap(new PositionDTO(army.getToX(), army.getToY()));
			if (posArmys.size() > 0) {
				posArmys.remove(army.getId());
			}
		}

		if (army.getCmd() != ArmyCmd.ArmyCmdIdle.getValue()) {
			armyMgr.getOutArmy(army.getId()).add(warArmy);
		} else {
			armyMgr.getOutArmy(army.getId()).remove(army);
		}
	}

	/**
	 * 军队到达目的地
	 *
	 * @param warArmy
	 */
	@Async(AsyncThreadPoolConfig.ASYNC_TASK_EXECUTOR_NAME)
	public void exeArrive(WarArmyDTO warArmy) {
		ArmyDTO army = warArmy.getArmy();

		switch (EnumUtils.fromValue(ArmyCmd.class, army.getCmd())) {
			case ArmyCmdAttack:
				if (!mapPositionLogic.isCanArrive(army.getRid(), army.getToX(), army.getToY()) ||
						mapPositionLogic.isWarFree(army.getToX(), army.getToY()) ||
						mapPositionLogic.isCanDefend(army.getRid(), army.getToX(), army.getToY())) {
					WarReportDTO emptyWar = armyWarLogic.newEmptyWar(warArmy);
					warReportDataPusher.syncExecute(null, emptyWar);
				} else {
					armyWarLogic.newBattle(warArmy);
				}
				armyLogic.armyBack(warArmy);
				break;
			case ArmyCmdDefend:
				//呆在哪里不动
				if (mapPositionLogic.isCanDefend(army.getRid(), army.getToX(), army.getToY())) {
					//目前是自己的领地才能驻守
					army.setState(ArmyState.ArmyStop.getValue());
					armyMgr.add2StopInPosArmyMap(warArmy);
					updateArmys(warArmy);
				} else {
					WarReportDTO emptyWar = armyWarLogic.newEmptyWar(warArmy);
					warReportDataPusher.syncExecute(null, emptyWar);

					armyLogic.armyBack(warArmy);
				}
				break;
			case ArmyCmdReclamation:
				if (army.getState().equals(ArmyState.ArmyRunning.getValue())) {
					if (nationMapLogic.buildIsRId(army.getRid(), army.getToX(), army.getToY())) {
						//目前是自己的领地才能屯田
						armyMgr.add2StopInPosArmyMap(warArmy);
						army.setState(ArmyState.ArmyStop.getValue());
						army.setCmd(ArmyCmd.ArmyCmdReclamation.getValue());
					} else {
						WarReportDTO emptyWar = armyWarLogic.newEmptyWar(warArmy);
						warReportDataPusher.syncExecute(null, emptyWar);

						armyLogic.armyBack(warArmy);
					}

				} else {
					armyLogic.armyBack(warArmy);
					//增加场量
					RoleResourceData rr = roleDataManager.getRoleResourceData(army.getRid());
					if (rr != null) {
						MapBuildDTO b = buildMgr.getPositionBuild(army.getToX(), army.getToY());
						if (b != null) {
							rr.setStone(rr.getStone() + b.getStone());
							rr.setIron(rr.getIron() + b.getIron());
							rr.setWood(rr.getWood() + b.getWood());
							rr.setGold(rr.getGold() + rr.getGold());
							rr.setGrain(rr.getGrain() + b.getGrain());
							roleDataPusher.syncExecute(rr.getRid(), rr);
						}
					}
				}
				break;
			case ArmyCmdBack:
				army.setState(ArmyState.ArmyStop.getValue());
				army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
				army.setTo(army.getFromX(), army.getFromY());
				updateArmys(warArmy);
				break;
			case ArmyCmdTransfer:
				//调动到位置了
				if (army.getState().equals(ArmyState.ArmyRunning.getValue())) {

					if (!nationMapLogic.buildIsRId(army.getRid(), army.getToX(), army.getToY())) {
						armyLogic.armyBack(warArmy);
					} else {
						MapBuildDTO b = buildMgr.getPositionBuild(army.getToX(), army.getToY());
						if (buildMgr.isHasTransferAuth(b)) {
							army.setState(ArmyState.ArmyStop.getValue());
							army.setCmd(ArmyCmd.ArmyCmdIdle.getValue());
							army.swapFromTo();
							armyMgr.add2StopInPosArmyMap(warArmy);
							updateArmys(warArmy);
						} else {
							armyLogic.armyBack(warArmy);
						}
					}
				}
				break;
			default:
				break;
		}
	}
}