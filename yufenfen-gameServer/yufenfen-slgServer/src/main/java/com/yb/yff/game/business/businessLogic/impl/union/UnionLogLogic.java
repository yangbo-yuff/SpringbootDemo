package com.yb.yff.game.business.businessLogic.impl.union;

import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.UnionMgrImpl;
import com.yb.yff.game.data.constant.myEnum.UnionMember;
import com.yb.yff.game.data.constant.myEnum.UnionOperationType;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.union.UnionApplyDTO;
import com.yb.yff.game.data.dto.union.UnionDTO;
import com.yb.yff.game.data.dto.union.UnionLogDTO;

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
 * @Class: UnionLogLogic
 * @CreatedOn 2024/12/14.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟日志逻辑
 */
public class UnionLogLogic {

	private final RoleDataMgrImpl roleDataMgr;
	private final UnionMgrImpl unionMgr;

	public UnionLogLogic(RoleDataMgrImpl roleDataMgr, UnionMgrImpl unionMgr) {

		this.roleDataMgr = roleDataMgr;
		this.unionMgr = unionMgr;
	}

	/**
	 * @param op
	 * @return
	 */
	public boolean newCreateLog(RoleDTO op) {
		String opNickName = op.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, 0,
				UnionOperationType.UnionOpCreate.getValue(),
				opNickName + " 创建了联盟", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * 解散联盟日志
	 *
	 * @param union
	 */
	public boolean newDismissLog(UnionDTO union) {
		RoleDTO chairman = roleDataMgr.getRoleDTO(union.getChairman());
		String opNickName = chairman.getNickName();

		UnionLogDTO ulog = new UnionLogDTO(union.getId(), chairman.getRid(), 0,
				UnionOperationType.UnionOpDismiss.getValue(),
				opNickName + " 解散了联盟", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * 加入联盟日志
	 *
	 * @param op
	 * @param apply
	 */
	public boolean newJoinLog(RoleDTO op, UnionApplyDTO apply) {
		String targetNickName = op.getNickName();
		Integer unionId = apply.getUnionId();
		Integer opRId = op.getRid();
		Integer targetId = apply.getRid();
		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, targetId, UnionOperationType.UnionOpJoin.getValue(),
				targetNickName + " 加入了联盟", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * @param op
	 * @return
	 */
	public boolean newExitLog(RoleDTO op) {
		String opNickName = op.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, opRId, UnionOperationType.UnionOpExit.getValue(),
				opNickName + " 退出了联盟", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * @param op
	 * @param target
	 * @return
	 */
	public boolean newKickLog(RoleDTO op, RoleDTO target) {
		String opNickName = op.getNickName();
		String targetNickName = target.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();
		Integer targetId = target.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, targetId, UnionOperationType.UnionOpKick.getValue(),
				opNickName + " 将 " + targetNickName + " 踢出了联盟", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * @param op
	 * @param target
	 * @param member
	 * @return
	 */
	public boolean newAppointLog(RoleDTO op, RoleDTO target, UnionMember member) {

		String opNickName = op.getNickName();
		String targetNickName = target.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();
		Integer targetId = target.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, targetId, UnionOperationType.UnionOpAppoint.getValue(),
				opNickName + " 将 " + targetNickName + " 任命为 " + member.getTitle(), System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * @param op
	 * @param target
	 * @param member
	 * @return
	 */
	public boolean newAbdicateLog(RoleDTO op, RoleDTO target, UnionMember member) {

		String opNickName = op.getNickName();
		String targetNickName = target.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();
		Integer targetId = target.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, targetId, UnionOperationType.UnionOpAbdicate.getValue(),
				opNickName + " 将 " + member.getTitle() + " 禅让给 " + targetNickName, System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}

	/**
	 * @param op
	 * @return
	 */
	public boolean newModNoticeLog(RoleDTO op) {
		String opNickName = op.getNickName();
		Integer unionId = op.getUnionId();
		Integer opRId = op.getRid();

		UnionLogDTO ulog = new UnionLogDTO(unionId, opRId, 0, UnionOperationType.UnionOpModNotice.getValue(),
				opNickName + " 修改了公告", System.currentTimeMillis());

		return unionMgr.saveUnionLog2DB(ulog);
	}
}
