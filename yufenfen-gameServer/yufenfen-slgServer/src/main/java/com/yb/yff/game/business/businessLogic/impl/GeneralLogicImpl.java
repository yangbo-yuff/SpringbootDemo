package com.yb.yff.game.business.businessLogic.impl;

import com.yb.yff.game.business.businessDataMgr.impl.GeneralMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.SKillMgrImpl;
import com.yb.yff.game.business.businessLogic.ICityLogic;
import com.yb.yff.game.business.businessLogic.IGeneralLogic;
import com.yb.yff.game.business.businessLogic.IRoleLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.constant.StaticConf;
import com.yb.yff.game.data.constant.myEnum.GeneralState;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.general.*;
import com.yb.yff.game.data.dto.role.RoleResourceDTO;
import com.yb.yff.game.data.dto.skill.SkillDTO;
import com.yb.yff.game.data.dto.skill.SkillLevelDTO;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/1.
 * @Email: yangboyff@gmail.com
 * @Description: 地图军队管理
 */
@Service
public class GeneralLogicImpl extends BusinessDataSyncImpl<GeneralDTO> implements IGeneralLogic {

	@Autowired
	GeneralMgrImpl generalMgrImpl;

	@Autowired
	SKillMgrImpl skillMgrImpl;

	@Autowired
	IRoleLogic roleLogic;

	@Autowired
	ICityLogic cityLogic;

	@Autowired
	IPushService pushService;

	@Autowired
	BusinessDataSyncImpl<SkillDTO> skillPusher;

	@PostConstruct
	public void init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_general, pushService);
	}


	/**
	 * 获取当前角色所有将领
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public List<GeneralDTO> getRoleGenerals(Integer rid) {
		return generalMgrImpl.getRoleGenerals(rid);
	}

	/**
	 * 抽卡
	 *
	 * @param rid
	 * @param drawTimes
	 * @return
	 */
	@Override
	public List<GeneralDTO> drawGeneral(Integer rid, Integer drawTimes) {
		// 随机抽取将领 ID
		List<Integer> cfgIdList = generalMgrImpl.getGCGidlList();
		List<Integer> drawGeneralIds = new ArrayList<>();

		while (drawGeneralIds.size() < 10) {
			Random random = new Random();
			int index = random.nextInt(cfgIdList.size());
			drawGeneralIds.add(cfgIdList.get(index));
		}
		CityDTO mainCity = cityLogic.getMainCitys(rid);
		if (mainCity == null) {
			return null;
		}

		// 根据ID 生成 将领信息
		List<GeneralDTO> generalList = generalMgrImpl.createGeneral(rid, mainCity.getCityId(), drawGeneralIds);

		// 招募花费
		Integer spending = 30 * generalList.size();
		RoleResourceDTO roleResourceDTO = new RoleResourceDTO();
		roleResourceDTO.setGold(spending);
		roleLogic.updateRoleResource(rid, roleResourceDTO, false);

		return generalList;
	}

	/**
	 * 准备流放
	 *
	 * @param rid
	 * @param convertIDs
	 * @return 执行流放的将领
	 */
	@Override
	public ResponseCode convertGeneral(Integer rid, List<Integer> convertIDs, ConvertResDTO convertRes) {
		List<GeneralDTO> convertGenerals = generalMgrImpl.selectGenerals(convertIDs);

		List<Integer> killIDs = new ArrayList<>();

		// 流放回收
		AtomicInteger recovering = new AtomicInteger();

		convertGenerals.stream().forEach(general -> {
			// 只有 不在军队中 的将领可以流放
			if (general.getOrder() == 0) {
				general.setState(GeneralState.GeneralConvert.getValue());

				if (generalMgrImpl.updateGeneralState(general)) {
					recovering.addAndGet(10 * (general.getStar()) * (1 + general.getStar_lv()));
					killIDs.add(general.getId());
				}
			}
		});

		// 更新角色资源, 仅更新金币
		RoleResourceDTO roleResource = new RoleResourceDTO();
		roleResource.setGold(recovering.get());
		roleLogic.updateRoleResource(rid, roleResource, true);

		convertRes.setGold(roleLogic.getRole(rid).getGold());
		convertRes.setAdd_gold(recovering.get());
		convertRes.setGIds(killIDs);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 合成
	 *
	 * @param rid
	 * @param composeGeneral
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<GeneralDTO>> composeGeneral(Integer rid, ComposeGeneralDTO composeGeneral) {
		LogicTaskResultDTO<List<GeneralDTO>> rsp = new LogicTaskResultDTO<>();

		GeneralDTO gs = generalMgrImpl.getRoleGeneral(composeGeneral.getCompId());

		if (gs == null || !gs.getRid().equals(rid)) {
			rsp.setCode(NetResponseCodeConstants.GeneralNoHas);
			return rsp;
		}

		//是否都有这个武将
		List<GeneralDTO> gss = hasGenerals(rid, composeGeneral.getGIds());
		if (gss.size() != composeGeneral.getGIds().size()) {
			rsp.setCode(NetResponseCodeConstants.GeneralNoHas);
			return rsp;
		}

		//是否同一个类型的武将
		for (GeneralDTO t : gss) {
			if (!t.getCfgId().equals(gs.getCfgId())) {
				rsp.setCode(NetResponseCodeConstants.GeneralNoSame);
				return rsp;
			}
		}

		//是否超过武将星级
		if ((gs.getStar() - gs.getStar_lv()) < gss.size()) {
			rsp.setCode(NetResponseCodeConstants.GeneralStarMax);
			return rsp;
		}

		gs.setStar_lv(gs.getStar_lv() + gss.size());
		gs.setHasPrPoint(gs.getHasPrPoint() + gss.size());
		this.syncExecute(rid, gs);

		for (GeneralDTO t : gss) {
			t.setParentId(gs.getId());
			t.setState(GeneralState.GeneralComposeStar.getValue());
			this.syncExecute(rid, t);
		}

		rsp.setResult(gss);

		rsp.setCode(NetResponseCodeConstants.SUCCESS);
		return rsp;
	}

	/**
	 * 分配属性点
	 *
	 * @param rid
	 * @param addPrGeneralReq
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<GeneralDTO> addPrGeneral(Integer rid, AddPrGeneralReqDTO addPrGeneralReq) {
		LogicTaskResultDTO<GeneralDTO> rsp = new LogicTaskResultDTO<>();

		GeneralDTO gs = hasGeneral(rid, addPrGeneralReq.getCompId());
		//是否有这个武将
		if (gs == null) {
			rsp.setCode(NetResponseCodeConstants.GeneralNoHas);
			return rsp;
		}

		Integer all = addPrGeneralReq.getForceAdd() + addPrGeneralReq.getStrategyAdd() + addPrGeneralReq.getDefenseAdd()
				+ addPrGeneralReq.getSpeedAdd() + addPrGeneralReq.getDestroyAdd();
		if (gs.getHasPrPoint() < all) {
			rsp.setCode(NetResponseCodeConstants.DBError);
			return rsp;
		}

		gs.setForce_added(addPrGeneralReq.getForceAdd());
		gs.setStrategy_added(addPrGeneralReq.getStrategyAdd());
		gs.setDefense_added(addPrGeneralReq.getDefenseAdd());
		gs.setSpeed_added(addPrGeneralReq.getSpeedAdd());
		gs.setDestroy_added(addPrGeneralReq.getDestroyAdd());
		gs.setUsePrPoint(all);
		this.syncExecute(rid, gs);

		rsp.setResult(gs);

		rsp.setCode(NetResponseCodeConstants.SUCCESS);
		return rsp;
	}

	/**
	 * 分配技能
	 *
	 * @param rid
	 * @param killReq
	 * @return
	 */
	@Override
	public ResponseCode upGeneralKill(Integer rid, UpDownSkillReqDTO killReq) {
		if (killReq.getPos() < 0 || killReq.getPos() >= StaticConf.SKILL_LIMIT) {
			return NetResponseCodeConstants.InvalidParam;
		}

		GeneralDTO g = hasGeneral(rid, killReq.getGId());
		if (g == null) {
			return NetResponseCodeConstants.GeneralNotMe;
		}

		LogicTaskResultDTO<SkillDTO> resultDTO = skillMgrImpl.getSkillOrCreate(rid, killReq.getCfgId());
		if (resultDTO.getCode() != NetResponseCodeConstants.SUCCESS) {
			return resultDTO.getCode();
		}

		SkillDTO skill = resultDTO.getResult();

		// 不能限制技能，人人都可以学
//		if skill.IsInLimit() == false {
//			rsp.Body.Code = constant.OutSkillLimit
//			return
//		}

		if (!skillMgrImpl.checkSKillsArmy(g.getCurArms(), killReq.getCfgId())) {
			return NetResponseCodeConstants.OutArmNotMatch;
		}

		if (!upSkill(g, skill.getId(), killReq.getCfgId(), killReq.getPos())) {
			return NetResponseCodeConstants.UpSkillError;
		}

		this.syncExecute(rid, g);

		skill.getGenerals().add(g.getId());
		skillMgrImpl.addRoleSkill2Map(rid, skill);

		skillPusher.syncExecute(rid, skill);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 分配技能
	 *
	 * @param rid
	 * @param killReq
	 * @return
	 */
	@Override
	public ResponseCode downGeneralKill(Integer rid, UpDownSkillReqDTO killReq) {
		if (killReq.getPos() < 0 || killReq.getPos() >= StaticConf.SKILL_LIMIT) {
			return NetResponseCodeConstants.InvalidParam;
		}

		GeneralDTO g = hasGeneral(rid, killReq.getGId());
		if (g == null) {
			return NetResponseCodeConstants.GeneralNotMe;
		}

		LogicTaskResultDTO<SkillDTO> resultDTO = skillMgrImpl.getSkillOrCreate(rid, killReq.getCfgId());
		if (resultDTO.getCode() != NetResponseCodeConstants.SUCCESS) {
			return resultDTO.getCode();
		}

		SkillDTO skill = resultDTO.getResult();

		if (!downSkill(g, skill.getId(), killReq.getPos())) {
			return NetResponseCodeConstants.DownSkillError;
		}

		this.syncExecute(rid, g);

		skill.getGenerals().remove(g.getId());

		skillPusher.syncExecute(rid, skill);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 分配技能
	 *
	 * @param rid
	 * @param killReq
	 * @return
	 */
	@Override
	public ResponseCode lvGeneralKill(Integer rid, LvSkillReqDTO killReq) {
		GeneralDTO g = hasGeneral(rid, killReq.getGId());
		if (g == null) {
			return NetResponseCodeConstants.GeneralNotMe;
		}

		LogicTaskResultDTO<SkillLevelDTO> posResult = posSkill(g, killReq.getPos());
		if (posResult.getCode() != NetResponseCodeConstants.SUCCESS) {
			return posResult.getCode();
		}

		SkillLevelDTO gSkill = posResult.getResult();
		if (gSkill == null) {
			return NetResponseCodeConstants.PosNotSkill;
		}

		ResponseCode checkRes = skillMgrImpl.checkSkillLevel(gSkill);
		if (checkRes != NetResponseCodeConstants.SUCCESS) {
			return checkRes;
		}

		gSkill.setLv(gSkill.getLv() + 1);

		this.syncExecute(rid, g);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param generalDTO
	 */
	@Override
	public void syncExecute(Integer rid, GeneralDTO generalDTO) {
		generalMgrImpl.updateGeneral2DB(generalDTO);

		pushData(generalDTO.getRid(), generalDTO);
	}

	/**
	 * 判断是否有这些武将
	 *
	 * @param rid
	 * @param gIds
	 * @return
	 */
	private List<GeneralDTO> hasGenerals(Integer rid, List<Integer> gIds) {
		List<GeneralDTO> gs = new ArrayList<>();

		for (Integer gid : gIds) {
			GeneralDTO g = generalMgrImpl.getRoleGeneral(gid);
			if (g == null || !g.getRid().equals(rid)) {
				return gs;
			}

			gs.add(g);
		}

		return gs;
	}

	private GeneralDTO hasGeneral(Integer rid, Integer gid) {
		GeneralDTO gs = generalMgrImpl.getRoleGeneral(gid);

		if (gid == null || !rid.equals(gs.getRid())) {
			return null;
		}

		return gs;
	}

	private boolean upSkill(GeneralDTO general, Integer skillId, Integer cfgId, Integer pos) {
		if (pos < 0 || pos >= StaticConf.SKILL_LIMIT) {
			return false;
		}

		for (SkillLevelDTO skillLevel : general.getSkills()) {
			if (skillLevel != null && skillLevel.getId().equals(skillId)) {
				//已经上过同类型的技能了
				return false;
			}
		}

		SkillLevelDTO s = general.getPosSkill(pos);
		if (s == null) {
			SkillLevelDTO newSL = new SkillLevelDTO(skillId, 1, cfgId);
			general.setPosSkill(pos, newSL);
			return true;
		}

		if (s.getId() != 0) {
			return false;
		}

		s.setId(skillId);
		s.setCfgId(cfgId);
		s.setLv(1);

		return true;
	}


	private boolean downSkill(GeneralDTO general, Integer skillId, Integer pos) {
		if (pos < 0 || pos >= StaticConf.SKILL_LIMIT) {
			return false;
		}

		SkillLevelDTO skillLevelDTO = general.getPosSkill(pos);
		if (skillLevelDTO == null || !skillLevelDTO.getId().equals(skillId)) {
			return false;
		}


		skillLevelDTO.setId(0);
		skillLevelDTO.setLv(0);
		skillLevelDTO.setCfgId(0);
		return true;
	}

	private LogicTaskResultDTO<SkillLevelDTO> posSkill(GeneralDTO general, Integer pos) {
		LogicTaskResultDTO<SkillLevelDTO> rsp = new LogicTaskResultDTO<>();
		if (pos >= general.getSkills().length) {
			rsp.setCode(NetResponseCodeConstants.InvalidParam);
			return rsp;
		}
		rsp.setCode(NetResponseCodeConstants.SUCCESS);
		rsp.setResult(general.getPosSkill(pos));

		return rsp;
	}
}
