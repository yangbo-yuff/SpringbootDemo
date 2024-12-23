package com.yb.yff.game.business.businessLogic.impl.war;

import com.yb.yff.game.business.businessDataMgr.impl.WarReportMgrImpl;
import com.yb.yff.game.business.businessLogic.IWarReportLogic;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.constant.myEnum.PushTaskType;
import com.yb.yff.game.data.dto.LogicTaskResultDTO;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.game.service.IPushService;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @Class: WarReportLogicImpl
 * @CreatedOn 2024/12/22.
 * @Email: yangboyff@gmail.com
 * @Description: 战争报告逻辑
 */
@Service
@Slf4j
public class WarReportLogicImpl extends BusinessDataSyncImpl<WarReportDTO> implements IWarReportLogic {
	final WarReportMgrImpl warReportMgr;

	final IPushService pushService;

	@Autowired
	public WarReportLogicImpl(WarReportMgrImpl warReportMgr, IPushService pushService) {
		this.warReportMgr = warReportMgr;
		this.pushService = pushService;
	}


	@PostConstruct
	public void init() {
		this.initBusinessPusher(PushTaskType.PUSH_TASK_TYPE_warReport, pushService);
	}

	/**
	 * 获取最近N次战斗报告
	 *
	 * @param rid
	 * @param lastNum
	 * @return
	 */
	@Override
	public LogicTaskResultDTO<List<WarReportDTO>> getLastWarReport(Integer rid, Integer lastNum) {
		LogicTaskResultDTO<List<WarReportDTO>> result = new LogicTaskResultDTO<>();

		List<WarReportDTO> reports = warReportMgr.selectWarReport(rid, lastNum);
		if (reports == null) {
			result.setCode(NetResponseCodeConstants.DBError);
			return result;
		}

		result.setResult(reports);

		result.setCode(NetResponseCodeConstants.SUCCESS);

		return result;
	}

	/**
	 * 阅读战斗报告
	 *
	 * @param rid
	 * @param reportId
	 * @return
	 */
	@Override
	public ResponseCode readWarReport(Integer rid, Integer reportId) {
		Boolean reslut = false;
		try {
			if (reportId == 0) {
				reslut = warReportMgr.readWarReportAll(rid);
			} else {
				reslut = warReportMgr.readWarReport(rid, reportId);
			}
		} catch (Exception e) {
			e.printStackTrace();

			return NetResponseCodeConstants.DBError;
		}

		if (reslut) {
			return NetResponseCodeConstants.SUCCESS;
		}

		return NetResponseCodeConstants.InvalidParam;
	}

	/**
	 * 数据同步
	 *
	 * @param rid
	 * @param warReport
	 */
	@Override
	public void syncExecute(Integer rid, WarReportDTO warReport) {
		// 2 DB
		warReportMgr.updateWartReport2DB(warReport);

		// 攻击方
		pushData(warReport.getA_rid(), warReport);

		// 防守方，0为系统，无需推送结果
		if (warReport.getD_rid() == 0) {
			pushData(warReport.getD_rid(), warReport);
		}
	}
}
