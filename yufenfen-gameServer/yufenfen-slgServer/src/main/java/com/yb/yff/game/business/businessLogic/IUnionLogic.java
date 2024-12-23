package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.union.*;
import com.yb.yff.sb.constant.ResponseCode;

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
 * @Class: IUnionLogic
 * @CreatedOn 2024/12/12.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟逻辑
 */
public interface IUnionLogic {
	/**
	 * 创建联盟
	 *
	 * @param rid
	 * @param name
	 */
	LogicTaskResultDTO<UnionDTO> createUnion(Integer rid, String name);

	/**
	 * 获取联盟列表
	 * @return
	 */
	List<UnionResDTO> getUnionResList();


	/**
	 * 获取联盟成员列表
	 * @param unionId
	 * @return
	 */
	LogicTaskResultDTO<List<MemberDTO>> getUnionMembers(Integer unionId);

	/**
	 * 获取联盟详情
	 * @param unionId
	 * @return
	 */
	LogicTaskResultDTO<UnionResDTO> getUnionInfo(Integer unionId);

	/**
	 * 加入联盟
	 *
	 * @param rid
	 * @param unionId
	 * @return
	 */
	LogicTaskResultDTO<ResponseCode> joinUnion(Integer rid, Integer unionId);

	/**
	 * 审核申请人
	 *
	 * @param rid
	 * @param verifyInfo
	 * @return
	 */
	ResponseCode verifyApplicant(Integer rid, VerifyReqDTO verifyInfo);

	/**
	 * 退出联盟
	 *
	 * @param rid
	 * @return
	 */
	ResponseCode exitUnion(Integer rid);

	/**
	 * 踢出玩家
	 * @param opRid
	 * @param targetId
	 * @return
	 */
	ResponseCode kickUnion(Integer opRid, Integer targetId);

	/**
	 * 任命
	 * @param opRid
	 * @param appointInfo
	 * @return
	 */
	ResponseCode appointUnion(Integer opRid, AppointReqDTO appointInfo);

	/**
	 * 禅让
	 * @param opRid
	 * @param tagetId
	 * @return
	 */
	ResponseCode abdicateUnion(Integer opRid, Integer tagetId);

	/**
	 * 修改公告
	 * @param rid
	 * @param text
	 * @return
	 */
	ResponseCode modNoticeUnion(Integer rid, String text);

	/**
	 * 获取玩家所在联盟的日志列表
	 *
	 * @param rid
	 * @return
	 */
	LogicTaskResultDTO<List<UnionLogDTO>> getUnionLogList(Integer rid);

	/**
	 * 解散联盟
	 * @param rid
	 * @return
	 */
	ResponseCode dismissUnion(Integer rid);
}
