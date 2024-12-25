package com.yb.yff.game.business.businessDataMgr.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.data.constant.myEnum.UnionApplyState;
import com.yb.yff.game.data.constant.myEnum.UnionMember;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.union.MemberDTO;
import com.yb.yff.game.data.dto.union.UnionApplyDTO;
import com.yb.yff.game.data.dto.union.UnionDTO;
import com.yb.yff.game.data.dto.union.UnionLogDTO;
import com.yb.yff.game.data.entity.CoalitionApplyEntity;
import com.yb.yff.game.data.entity.CoalitionEntity;
import com.yb.yff.game.data.entity.CoalitionLogEntity;
import com.yb.yff.game.service.ICoalitionApplyService;
import com.yb.yff.game.service.ICoalitionLogService;
import com.yb.yff.game.service.ICoalitionService;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: UnionMgrImpl
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟数据管理
 */
@Component
@Slf4j
public class UnionMgrImpl implements IJsonDataHandler {
	@Autowired
	ICoalitionService coalitionService;

	@Autowired
	ICoalitionLogService coalitionLogService;

	@Autowired
	ICoalitionApplyService coalitionApplyService;

	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	CityMgrImpl cityMgr;


	ConcurrentHashMap<Integer, UnionDTO> dbUnionMap = new ConcurrentHashMap<>();

	/**
	 * key: unionId
	 */
	ConcurrentHashMap<Integer, List<UnionLogDTO>> dbUnionLogMap = new ConcurrentHashMap<>();

	/**
	 * key: unionId, dbId
	 */
	ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, UnionApplyDTO>> dbUnionApplyMap = new ConcurrentHashMap<>();

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {
		if (dbUnionMap.size() > 0) {
			return;
		}

		List<CoalitionEntity> dbList = coalitionService.getBaseMapper().selectList(null);

		// 查询数据库中是否有记录
		if (dbList.size() == 0) {
			return;
		}

		dbList.forEach(entity -> {
			addUnion2Cache(unionEntity2DTO(entity));
		});
	}

	private UnionDTO unionEntity2DTO(CoalitionEntity entity) {
		UnionDTO dto = new UnionDTO();
		BeanUtils.copyProperties(entity, dto);

		if (!StringUtil.isNullOrEmpty(entity.getMembers())) {
			List<Integer> members = JSONArray.parseArray(entity.getMembers(), Integer.class);
			dto.setMembers(members);
		}
		List<MemberDTO> memberList = getMembers(dto);
		dto.setMemberList(memberList);

		return dto;
	}



	/**
	 * 获取联盟成员列表
	 *
	 * @param unionDTO
	 * @return
	 */
	public List<MemberDTO> getMembers(UnionDTO unionDTO) {
		List<MemberDTO> major = new ArrayList<>();
		unionDTO.getMembers().forEach(rid -> {
			MemberDTO member = new MemberDTO();
			member.setRid(rid);
			member.setName(roleDataMgr.getRoleDTO(rid).getNickName());

			CityDTO city = cityMgr.getCity(rid);
			member.setX(city.getX());
			member.setY(city.getY());

			if (rid.equals(unionDTO.getChairman())) {
				member.setTitle(UnionMember.UnionChairman.getValue());
			} else if (rid.equals(unionDTO.getViceChairman())) {
				member.setTitle(UnionMember.UnionViceChairman.getValue());
			} else {
				member.setTitle(UnionMember.UnionCommon.getValue());
			}
			major.add(member);
		});

		return major;
	}

	private CoalitionEntity unionDTO2Entity(UnionDTO dto) {
		CoalitionEntity entity = new CoalitionEntity();
		BeanUtils.copyProperties(dto, entity);

		entity.setMembers(JSONArray.toJSONString(dto.getMembers()));

		return entity;
	}

	private CoalitionApplyEntity unionApplyDTO2Entity(UnionApplyDTO dto) {
		CoalitionApplyEntity entity = new CoalitionApplyEntity();
		BeanUtils.copyProperties(dto, entity);

		return entity;
	}

	private UnionApplyDTO unionApplyEntity2DTO(CoalitionApplyEntity entity) {
		UnionApplyDTO dto = new UnionApplyDTO();
		BeanUtils.copyProperties(entity, dto);

		return dto;
	}

	private CoalitionLogEntity CoalitionLogDTO2Entity(UnionLogDTO dto) {
		CoalitionLogEntity entity = new CoalitionLogEntity();
		BeanUtils.copyProperties(dto, entity);

		return entity;
	}

	private UnionLogDTO CoalitionLogEntity2DTO(CoalitionLogEntity entity) {
		UnionLogDTO dto = new UnionLogDTO();
		BeanUtils.copyProperties(entity, dto);

		dto.setOpRid(entity.getOpRid());
		dto.setTargetId(entity.getTargetId());

		return dto;
	}

	public void addUnion2Cache(UnionDTO unionDTO) {
		// 配置联盟ID
		unionDTO.getMembers().forEach(memberId -> {
			RoleDTO role = roleDataMgr.getRoleDTO(memberId);
			role.setUnionId(unionDTO.getId());

			List<CityDTO> citys = cityMgr.getCitys(memberId);
//			if(citys != null && citys.size() > 0){
				citys.forEach(city -> {
					city.setUnionId(unionDTO.getId());
					city.setUnionName(unionDTO.getName());
				});
//			}
		});

		dbUnionMap.put(unionDTO.getId(), unionDTO);
	}

	public UnionDTO getUnion(Integer unionId) {
		if(unionId == null || unionId == 0){
			return null;
		}
		return dbUnionMap.get(unionId);
	}

	public UnionDTO getRoleUnion(Integer rid) {
		RoleDTO role = roleDataMgr.getRoleDTO(rid);
		if (role == null) {
			return null;
		}

		Integer unionId = role.getUnionId();
		if (unionId == null || unionId <= 0) {
			return null;
		}

		return dbUnionMap.get(unionId);
	}

	/**
	 * 移除联盟
	 * @param unionId
	 * @return
	 */
	public UnionDTO removeUnion(Integer unionId) {

		return dbUnionMap.remove(unionId);
	}

	public List<UnionDTO> getUnions() {
		return dbUnionMap.values().stream().toList();
	}

	/**
	 * 联盟日志
	 * @param unionId
	 */
	public List<UnionLogDTO> getUnionLogs(Integer unionId){
		return dbUnionLogMap.computeIfAbsent(unionId, k -> loadLogsFromDB(unionId));
	}

	/**
	 * 联盟的申请记录
	 * @param unionId
	 */
	public ConcurrentHashMap<Integer, UnionApplyDTO> getUnionApplyMap(Integer unionId){
		return dbUnionApplyMap.computeIfAbsent(unionId, k -> loadApplysFromDB(unionId));
	}

	/**
	 * 联盟的申请记录
	 * @param unionId
	 */
	public List<UnionApplyDTO> getUnionApplyList(Integer unionId){

		return getUnionApplyMap(unionId).entrySet().stream().map(entry -> entry.getValue()).toList();
	}

	/**
	 * 联盟的申请记录
	 * @param unionId
	 */
	public UnionApplyDTO getUnionApply(Integer unionId, Integer dbId){
		return getUnionApplyMap(unionId).get(dbId);
	}

	/**
	 * 添加联盟的申请记录
	 * @param apply
	 */
	public void putUnionApplys(UnionApplyDTO apply){
		getUnionApplyMap(apply.getUnionId()).put(apply.getId(), apply);
	}

	public LogicTaskResultDTO<UnionApplyDTO> newUnionApply(Integer rid, Integer unionId){
		LogicTaskResultDTO<UnionApplyDTO> result = new LogicTaskResultDTO<>();

		RoleDTO role = roleDataMgr.getRoleDTO(rid);

		UnionApplyDTO apply = new UnionApplyDTO(unionId, rid, role.getNickName());

		// DB
		if(!updateUnionApply2DB(apply)){
			result.setCode(NetResponseCodeConstants.DBError);
		};

		// cache
		putUnionApplys(apply);

		result.setCode(NetResponseCodeConstants.SUCCESS);
		result.setResult(apply);

		return result;
	}

	public boolean updateUnionApply2DB(UnionApplyDTO dto) {
		CoalitionApplyEntity entity = unionApplyDTO2Entity(dto);

		try {
			boolean result = coalitionApplyService.save(entity);
			dto.setId(entity.getId());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveUnion2DB(UnionDTO unionDTO) {
		CoalitionEntity entity = unionDTO2Entity(unionDTO);
		try {
			boolean result = coalitionService.saveOrUpdate(entity);
			unionDTO.setId(entity.getId());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveUnionLog2DB(UnionLogDTO dto) {
		CoalitionLogEntity entity = CoalitionLogDTO2Entity(dto);
		try {
			return coalitionLogService.saveOrUpdate(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private synchronized List<UnionLogDTO> loadLogsFromDB(Integer unionId){
		QueryWrapper<CoalitionLogEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("union_id", unionId);
		List<CoalitionLogEntity> dbList = coalitionLogService.getBaseMapper().selectList(queryWrapper);

		List<UnionLogDTO> logs = new ArrayList<>();

		 dbList.forEach(entity -> {
			 UnionLogDTO logDTO = new UnionLogDTO();
			 BeanUtils.copyProperties(entity, logDTO);

			 logDTO.setOpRid(entity.getOpRid());
			 logDTO.setTargetId(entity.getTargetId());
			 logDTO.setCtime(entity.getCtime().getTime());

			 logs.add(logDTO);
		 });

		 return logs;
	}



	private synchronized ConcurrentHashMap<Integer, UnionApplyDTO> loadApplysFromDB(Integer unionId){
		QueryWrapper<CoalitionApplyEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("union_id", unionId);
		queryWrapper.eq("state", UnionApplyState.UnionUntreated.getValue());
		List<CoalitionApplyEntity> dbList = coalitionApplyService.getBaseMapper().selectList(queryWrapper);

		ConcurrentHashMap<Integer, UnionApplyDTO> applys = new ConcurrentHashMap<>();

		dbList.forEach(entity -> {
			UnionApplyDTO applyDTO = new UnionApplyDTO();
			BeanUtils.copyProperties(entity, applyDTO);

			RoleDTO role = roleDataMgr.getRoleDTO(entity.getRid());
			applyDTO.setNickName(role.getNickName());

			applys.put(applyDTO.getId(), applyDTO);
		});

		return applys;
	}
}
