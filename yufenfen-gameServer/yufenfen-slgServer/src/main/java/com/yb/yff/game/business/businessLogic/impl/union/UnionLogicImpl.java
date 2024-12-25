package com.yb.yff.game.business.businessLogic.impl.union;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.CityMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.UnionMgrImpl;
import com.yb.yff.game.business.businessLogic.IUnionApply;
import com.yb.yff.game.business.businessLogic.IUnionLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.constant.EnumUtils;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.constant.myEnum.UnionMember;
import com.yb.yff.game.data.constant.myEnum.UnionState;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.union.*;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
 * @Class: UnionLogicImpl
 * @CreatedOn 2024/12/12.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟逻辑
 */
@Service
public class UnionLogicImpl extends BusinessDataSyncImpl<UnionApplyDTO> implements IUnionLogic, IUnionApply {
	@Autowired
	RoleDataMgrImpl roleDataMgr;

	@Autowired
	UnionMgrImpl unionMgr;

	@Autowired
	CityMgrImpl cityMgr;

	@Autowired
	BusinessDataSyncImpl<CityDTO> cityPusher;

	@Autowired
	JsonConfigMgr jsonConfigMgr;

	@Autowired
	IPushService pushService;

	UnionLogLogic unionLogLogic;

	UnionApplyLogic unionApplyLogic;

	@PostConstruct
	public void init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_unionApply, pushService);

		unionLogLogic = new UnionLogLogic(roleDataMgr, unionMgr);

		unionApplyLogic = new UnionApplyLogic(roleDataMgr, unionMgr, unionLogLogic, jsonConfigMgr, this);
	}

	/**
	 * 创建联盟
	 *
	 * @param rid
	 * @param name
	 */
	@Override
	public LogicTaskResultDTO<UnionDTO> createUnion(Integer rid, String name) {
		LogicTaskResultDTO<UnionDTO> result = new LogicTaskResultDTO<>();

		if (roleDataMgr.hasUnion(rid)) {
			result.setCode(NetResponseCodeConstants.UnionAlreadyHas);
			return result;
		}

		UnionDTO union = new UnionDTO(name, rid);
		List<MemberDTO> memberList = unionMgr.getMembers(union);
		union.setMemberList(memberList);

		if (!unionMgr.saveUnion2DB(union)) {
			result.setCode(NetResponseCodeConstants.UnionCreateError);
			return result;
		}

		unionMgr.addUnion2Cache(union);
		roleDataMgr.setRoleUnion(rid, union.getId());

		memberEnter(rid, union.getCreateId());

		unionLogLogic.newCreateLog(roleDataMgr.getRoleDTO(rid));

		result.setResult(union);
		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}

	/**
	 * 获取联盟列表
	 *
	 * @return
	 */
	@Override
	public List<UnionResDTO> getUnionResList() {
		List<UnionResDTO> unionlist = new ArrayList<>();
		unionMgr.getUnions().forEach(unionDTO -> {
			UnionResDTO unionResDTO = unionDTO2ResDTO(unionDTO);

			unionlist.add(unionResDTO);
		});

		return unionlist;
	}

	/**
	 * 获取联盟成员列表
	 *
	 * @param unionId
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<MemberDTO>> getUnionMembers(Integer unionId) {

		LogicTaskResultDTO<List<MemberDTO>> result = new LogicTaskResultDTO<>();

		UnionDTO unionDTO = unionMgr.getUnion(unionId);
		if (unionDTO == null) {
			result.setCode(NetResponseCodeConstants.UnionNotFound);
			return result;
		}

		result.setResult(unionDTO.getMemberList());

		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}

	/**
	 * 获取联盟详情
	 *
	 * @param unionId
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<UnionResDTO> getUnionInfo(Integer unionId) {
		LogicTaskResultDTO<UnionResDTO> result = new LogicTaskResultDTO<>();

		UnionDTO unionDTO = unionMgr.getUnion(unionId);
		if (unionDTO == null) {
			result.setCode(NetResponseCodeConstants.UnionNotFound);
			return result;
		}

		result.setResult(unionDTO2ResDTO(unionDTO));
		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}

	/**
	 * 加入联盟
	 *
	 * @param rid
	 * @param unionId
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<ResponseCode> joinUnion(Integer rid, Integer unionId) {
		LogicTaskResultDTO<ResponseCode> result = new LogicTaskResultDTO<>();

		UnionDTO unionDTO = unionMgr.getUnion(unionId);
		if (unionDTO == null) {
			result.setCode(NetResponseCodeConstants.UnionNotFound);
			return result;
		}

		if (roleDataMgr.hasUnion(rid)) {
			result.setCode(NetResponseCodeConstants.UnionAlreadyHas);
			return result;
		}


		if (unionDTO.getMembers().size() >= jsonConfigMgr.getBasicConfig().getUnion().getMember_limit()) {
			result.setCode(NetResponseCodeConstants.PeopleIsFull);
			return result;
		}

		//判断当前是否已经有申请
		if (unionApplyLogic.hasApply(rid, unionId)) {
			result.setCode(NetResponseCodeConstants.HasApply);
			return result;
		}

		//写入申请列表
		LogicTaskResultDTO<UnionApplyDTO> resultDTO = unionMgr.newUnionApply(rid, unionId);

		UnionApplyDTO apply = resultDTO.getResult();


		//推送主、副盟主
		syncExecute(rid, apply);

		result.setCode(NetResponseCodeConstants.SUCCESS);
		return result;
	}

	/**
	 * 审核申请人
	 *
	 * @param rid
	 * @param verifyInfo
	 * @return
	 */
	@Override
	public ResponseCode verifyApplicant(Integer rid, VerifyReqDTO verifyInfo) {
		return unionApplyLogic.verifyApplicant(rid, verifyInfo);
	}

	/**
	 * 审核申请人
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public ResponseCode exitUnion(Integer rid) {

		if (!roleDataMgr.hasUnion(rid)) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		RoleDTO role = roleDataMgr.getRoleDTO(rid);

		UnionDTO u = unionMgr.getUnion(role.getUnionId());
		if (u == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		//盟主不能退出
		if (u.getChairman().equals(rid)) {
			return NetResponseCodeConstants.UnionNotAllowExit;
		}

		u.getMembers().remove(rid);

		if (u.getViceChairman().equals(rid)) {
			u.setViceChairman(0);
		}

		memberExit(rid);
		unionMgr.saveUnion2DB(u);

		unionLogLogic.newExitLog(role);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 踢出玩家
	 *
	 * @param opRid
	 * @param targetId
	 * @return
	 */
	@Override
	public ResponseCode kickUnion(Integer opRid, Integer targetId) {
		if (!roleDataMgr.hasUnion(opRid)) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		RoleDTO opAr = roleDataMgr.getRoleDTO(opRid);

		UnionDTO u = unionMgr.getUnion(opAr.getUnionId());
		if (u == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		if (!u.getChairman().equals(opRid) && !u.getViceChairman().equals(opRid)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		if (opRid.equals(targetId)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		RoleDTO targetRole = roleDataMgr.getRoleDTO(targetId);
		if (targetRole == null) {
			return NetResponseCodeConstants.RoleNotExist;
		}

		if (!targetRole.getUnionId().equals(u.getId())) {
			return NetResponseCodeConstants.NotBelongUnion;
		}

		u.getMembers().remove(targetId);

		if (u.getViceChairman().equals(targetId)) {
			u.setViceChairman(0);
		}

		memberExit(targetId);
		targetRole.setUnionId(0);

		unionMgr.saveUnion2DB(u);

		unionLogLogic.newKickLog(opAr, targetRole);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 任命
	 *
	 * @param opRid
	 * @param appointInfo
	 * @return
	 */
	@Override
	public ResponseCode appointUnion(Integer opRid, AppointReqDTO appointInfo) {
		if (!roleDataMgr.hasUnion(opRid)) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		RoleDTO opAr = roleDataMgr.getRoleDTO(opRid);

		UnionDTO u = unionMgr.getUnion(opAr.getUnionId());
		if (u == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		if (!u.getChairman().equals(opRid) && !u.getViceChairman().equals(opRid)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		if (opRid.equals(appointInfo.getRid())) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		RoleDTO targetRole = roleDataMgr.getRoleDTO(appointInfo.getRid());
		if (targetRole == null) {
			return NetResponseCodeConstants.RoleNotExist;
		}

		if (!targetRole.getUnionId().equals(u.getId())) {
			return NetResponseCodeConstants.NotBelongUnion;
		}

		UnionMember member = EnumUtils.fromValue(UnionMember.class, appointInfo.getTitle());
		switch (member) {
			case UnionViceChairman -> {
				u.setViceChairman(targetRole.getRid());

				unionMgr.saveUnion2DB(u);

				unionLogLogic.newAppointLog(opAr, targetRole, member);
			}
			case UnionCommon -> {

				if (u.getViceChairman().equals(targetRole.getRid())) {
					u.setViceChairman(0);
				}

				unionLogLogic.newAppointLog(opAr, targetRole, member);
			}
			default -> {
				return NetResponseCodeConstants.InvalidParam;
			}
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 禅让
	 *
	 * @param opRid
	 * @param tagetId
	 * @return
	 */
	@Override
	public ResponseCode abdicateUnion(Integer opRid, Integer tagetId) {
		if (!roleDataMgr.hasUnion(opRid)) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		RoleDTO opAr = roleDataMgr.getRoleDTO(opRid);

		UnionDTO u = unionMgr.getUnion(opAr.getUnionId());
		if (u == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		if (!u.getChairman().equals(opRid) && !u.getViceChairman().equals(opRid)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		if (opRid.equals(tagetId)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		RoleDTO targetRole = roleDataMgr.getRoleDTO(tagetId);
		if (targetRole == null) {
			return NetResponseCodeConstants.RoleNotExist;
		}

		if (!u.getChairman().equals(targetRole.getRid()) && !u.getViceChairman().equals(targetRole.getRid())) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		if (!targetRole.getUnionId().equals(u.getId())) {
			return NetResponseCodeConstants.NotBelongUnion;
		}


		if (u.getChairman().equals(opRid)) {
			u.setChairman(targetRole.getRid());
			if (u.getViceChairman().equals(targetRole.getRid())) {
				u.setViceChairman(0);
			}

			unionMgr.saveUnion2DB(u);

			unionLogLogic.newAbdicateLog(opAr, targetRole, UnionMember.UnionChairman);
		} else if (u.getViceChairman().equals(opRid)) {
			u.setViceChairman(targetRole.getRid());

			unionMgr.saveUnion2DB(u);

			unionLogLogic.newAbdicateLog(opAr, targetRole, UnionMember.UnionViceChairman);
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 修改公告
	 *
	 * @param rid
	 * @param text
	 * @return
	 */
	@Override
	public ResponseCode modNoticeUnion(Integer rid, String text) {
		if (!roleDataMgr.hasUnion(rid)) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		RoleDTO opAr = roleDataMgr.getRoleDTO(rid);

		UnionDTO u = unionMgr.getUnion(opAr.getUnionId());
		if (u == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		if (!u.getChairman().equals(rid) && !u.getViceChairman().equals(rid)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		u.setNotice(text);

		unionMgr.saveUnion2DB(u);

		unionLogLogic.newModNoticeLog(opAr);

		return NetResponseCodeConstants.SUCCESS;
	}

	/**
	 * 获取联盟日志列表
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<UnionLogDTO>> getUnionLogList(Integer rid) {
		LogicTaskResultDTO<List<UnionLogDTO>> logResDTO = new LogicTaskResultDTO<>();
		UnionDTO unionDTO = unionMgr.getRoleUnion(rid);
		if (unionDTO == null) {
			logResDTO.setCode(NetResponseCodeConstants.UnionNotFound);
			return logResDTO;
		}

		List<UnionLogDTO> logs = unionMgr.getUnionLogs(unionDTO.getId());
		logResDTO.setResult(logs);
		logResDTO.setCode(NetResponseCodeConstants.SUCCESS);

		return logResDTO;
	}

	/**
	 * 解散联盟
	 *
	 * @param rid
	 * @return
	 */
	@Override
	public ResponseCode dismissUnion(Integer rid) {

		UnionDTO union = unionMgr.getRoleUnion(rid);

		if (union == null) {
			return NetResponseCodeConstants.UnionNotFound;
		}

		//盟主才能解散
		if (!union.getChairman().equals(rid)) {
			return NetResponseCodeConstants.PermissionDenied;
		}

		unionMgr.removeUnion(union.getId());

		union.getMembers().forEach(memberRid -> {
			memberExit(memberRid);
		});
		union.getMembers().clear();
		union.setState(UnionState.UnionDismiss.getValue());

		unionMgr.saveUnion2DB(union);


		if (!unionLogLogic.newDismissLog(union)) {
			return NetResponseCodeConstants.DBError;
		}

		return NetResponseCodeConstants.SUCCESS;
	}

	private UnionResDTO unionDTO2ResDTO(UnionDTO unionDTO) {
		UnionResDTO unionResDTO = new UnionResDTO();
		BeanUtils.copyProperties(unionDTO, unionResDTO);

		List<MajorDTO> major = new ArrayList<>();
		for (Integer rid : unionDTO.getMembers()) {
			MajorDTO member = new MajorDTO();
			member.setRid(rid);
			member.setName(roleDataMgr.getRoleDTO(rid).getNickName());

			if (rid.equals(unionDTO.getChairman())) {
				member.setTitle(UnionMember.UnionChairman.getValue());
			} else if (rid.equals(unionDTO.getViceChairman())) {
				member.setTitle(UnionMember.UnionViceChairman.getValue());
			} else {
				continue;
			}
			major.add(member);
		}
		unionResDTO.setMajor(major);

		unionResDTO.setCnt(unionDTO.getMembers().size());

		return unionResDTO;
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param unionApply
	 */
	@Override
	public void syncExecute(Integer rid, UnionApplyDTO unionApply) {
		UnionDTO unionDTO = unionMgr.getUnion(unionApply.getUnionId());
		if(unionDTO == null){
			return;
		}

		// 盟主
		pushData(unionDTO.getChairman(), unionApply);

		// 副盟主
		if (unionDTO.getViceChairman() != null && unionDTO.getViceChairman() > 0) {
			pushData(unionDTO.getViceChairman(), unionApply);
		}
	}

	private void memberEnter(Integer rid, Integer unionId) {
		UnionDTO union = unionMgr.getUnion(unionId);
		if (union == null) {
			return;
		}

		RoleDTO role = roleDataMgr.getRoleDTO(rid);
		if (role == null) {
			return;
		}

		role.setUnionId(union.getId());

		// TODO 没搞懂啥逻辑， parentId 与 unionId 啥区别
//		if attr.ParentId == unionId {
//			this.DelChild(unionId, attr.RId)
//		}

		List<CityDTO> citys = cityMgr.getCitys(rid);
		if (citys == null || citys.size() == 0) {
			return;
		}

		citys.forEach(city -> {
			city.setUnionId(union.getId());
			city.setUnionName(union.getName());
			cityPusher.syncExecute(rid, city);
		});
	}


	/**
	 * 退出联盟
	 *
	 * @param rid
	 */
	private void memberExit(Integer rid) {
		RoleDTO role = roleDataMgr.getRoleDTO(rid);
		role.setUnionId(0);
		role.setParentId(0);
		roleDataMgr.updateRoleDataToDB(role);

		List<CityDTO> citys = cityMgr.getCitys(rid);
		if (citys == null || citys.size() == 0) {
			return;
		}

		citys.forEach(city -> {
			cityPusher.syncExecute(rid, city);
		});
	}

	/**
	 * 审核拒绝
	 *
	 * @param apply
	 */
	@Override
	public void onUnionRefuse(UnionApplyDTO apply) {

	}

	/**
	 * 审核通过
	 *
	 * @param apply
	 */
	@Override
	public void onUnionAdopt(UnionApplyDTO apply) {
		memberEnter(apply.getRid(), apply.getUnionId());
	}
}
