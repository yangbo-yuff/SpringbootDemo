package com.yb.yff.game.service.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.yff.game.business.businessDataMgr.impl.ArmyMgrImpl;
import com.yb.yff.game.business.businessLogic.INationMapLogic;
import com.yb.yff.game.constant.GameBusinessType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.ArmyDTO;
import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.nationMap.*;
import com.yb.yff.game.service.business.impl.base.BusinessServiceImpl;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.sb.data.dto.GameMessageEnhancedReqDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
 * @Class: GameRoleServiceImpl
 * @CreatedOn 2024/10/20.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏游戏国家地图业务服务
 */
@Service(GameBusinessType.GAME_BUSINESS_TYPE_NATIONMAP)
public class NationMapServiceImpl extends BusinessServiceImpl {

	@Autowired
	INationMapLogic nationMapLogic;

	@Autowired
	ArmyMgrImpl armyLogic;

	/**
	 * @param businessHandlerMap
	 */
	@Override
	public void initBusinessHandlerMap(Map<String, Function<GameMessageEnhancedReqDTO, GameBusinessResBaseDTO>> businessHandlerMap) {
		businessHandlerMap.put("config", this::doConfig);
		businessHandlerMap.put("scan", this::doScan);
		businessHandlerMap.put("scanBlock", this::doScanBlock);
		businessHandlerMap.put("giveUp", this::doGiveUp);
		businessHandlerMap.put("build", this::doBuild);
		businessHandlerMap.put("upBuild", this::doUpBuild);
		businessHandlerMap.put("delBuild", this::doDelBuild);
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doConfig(GameMessageEnhancedReqDTO reqDTO) {
		// TODO 根据 reqDTO 获得 配置 信息
		ConfigResDTO configResDTO = new ConfigResDTO();

		List<ConfigDTO> configs = nationMapLogic.getNationalMapConfig();
		configResDTO.setConfs(configs);

		configResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return configResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doScan(GameMessageEnhancedReqDTO reqDTO) {
		ScanBlockResDTO scanBlockResDTO = new ScanBlockResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			scanBlockResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return scanBlockResDTO;
		}

		ScanDTO scanDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ScanDTO.class);

		List<BuildDTO> roleBuildList = nationMapLogic.scan(scanDTO.getX(), scanDTO.getY(), CityPositionUtils.MapBuildBoundary, BuildDTO.class);
		scanBlockResDTO.setMrBuilds(roleBuildList);

		List<CityDTO> roleCityList = nationMapLogic.scan(scanDTO.getX(), scanDTO.getY(), CityPositionUtils.MapCityBoundary, CityDTO.class);
		scanBlockResDTO.setMcBuilds(roleCityList);

		scanBlockResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return scanBlockResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doScanBlock(GameMessageEnhancedReqDTO reqDTO) {
		ScanBlockResDTO scanBlockResDTO = new ScanBlockResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			scanBlockResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return scanBlockResDTO;
		}

		ScanBlockDTO scanBlockDTO = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), ScanBlockDTO.class);

		List<BuildDTO> roleBuildList = nationMapLogic.scanBlock(scanBlockDTO.getX(), scanBlockDTO.getY(), scanBlockDTO.getLength(), BuildDTO.class);
		scanBlockResDTO.setMrBuilds(roleBuildList);

		List<CityDTO> roleCityList = nationMapLogic.scanBlock(scanBlockDTO.getX(), scanBlockDTO.getY(), scanBlockDTO.getLength(), CityDTO.class);
		scanBlockResDTO.setMcBuilds(roleCityList);

		List<ArmyDTO> armys = new ArrayList<>();
		scanBlockResDTO.setArmys(armys);

		scanBlockResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());

		return scanBlockResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doGiveUp(GameMessageEnhancedReqDTO reqDTO) {

		GiveUpResDTO giveUpResDTO = new GiveUpResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			giveUpResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return giveUpResDTO;
		}

		GiveUpDTO giveUpPos = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), GiveUpDTO.class);
		BeanUtils.copyProperties(giveUpPos, giveUpResDTO);

		if (!nationMapLogic.buildIsRId(rid, giveUpPos.getX(), giveUpPos.getY())) {
			giveUpResDTO.setCode(NetResponseCodeConstants.BuildNotMe.getCode());
			return giveUpResDTO;
		}

		ResponseCode result = nationMapLogic.giveUp(rid, giveUpPos.getX(), giveUpPos.getY());

		giveUpResDTO.setCode(result.getCode());
		return giveUpResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doBuild(GameMessageEnhancedReqDTO reqDTO) {

		MapCellResDTO mapCellResDTO = new MapCellResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			mapCellResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return mapCellResDTO;
		}

		MapCellReqDTO buildPos = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), MapCellReqDTO.class);
		BeanUtils.copyProperties(buildPos, mapCellResDTO);

		ResponseCode result = nationMapLogic.build(buildPos, rid);
		if (result != NetResponseCodeConstants.SUCCESS) {
			mapCellResDTO.setCode(result.getCode());
			return mapCellResDTO;
		}

		mapCellResDTO.setCode(NetResponseCodeConstants.SUCCESS.getCode());
		return mapCellResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doUpBuild(GameMessageEnhancedReqDTO reqDTO) {
		MapBuildUpResDTO mapCellResDTO = new MapBuildUpResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			mapCellResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return mapCellResDTO;
		}

		MapBuildUpDTO buildPos = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), MapBuildUpDTO.class);
		mapCellResDTO.setX(buildPos.getX());
		mapCellResDTO.setY(buildPos.getY());

		LogicTaskResultDTO<BuildDTO> result = nationMapLogic.upBbuild(buildPos, rid);
		if (result.getCode() != NetResponseCodeConstants.SUCCESS) {
			mapCellResDTO.setCode(result.getCode().getCode());
			return mapCellResDTO;
		}

		mapCellResDTO.setBuild(result.getResult());

		return mapCellResDTO;
	}

	/**
	 * 执行业务
	 *
	 * @param reqDTO
	 * @return
	 */
	public GameBusinessResBaseDTO doDelBuild(GameMessageEnhancedReqDTO reqDTO) {
		MapBuildUpResDTO mapCellResDTO = new MapBuildUpResDTO();

		Integer rid = reqDTO.getRid();
		if (rid == null) {
			mapCellResDTO.setCode(NetResponseCodeConstants.RoleNotExist.getCode());
			return mapCellResDTO;
		}

		MapBuildUpDTO buildPos = JSONObject.toJavaObject((JSONObject) reqDTO.getMsg(), MapBuildUpDTO.class);
		mapCellResDTO.setX(buildPos.getX());
		mapCellResDTO.setY(buildPos.getY());

		LogicTaskResultDTO<BuildDTO> result = nationMapLogic.delBbuild(buildPos, rid);

		mapCellResDTO.setCode(result.getCode().getCode());

		mapCellResDTO.setBuild(result.getResult());

		return mapCellResDTO;
	}
}
