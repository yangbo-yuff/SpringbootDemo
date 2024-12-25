package com.yb.yff.game.business.businessDataMgr.impl;

import com.yb.yff.game.business.businessDataMgr.IJsonDataHandler;
import com.yb.yff.game.business.businessLogic.impl.base.BusinessDataSyncImpl;
import com.yb.yff.game.data.dto.army.WarReportDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.game.data.entity.WarReportEntity;
import com.yb.yff.game.mapper.WarReportMapper;
import com.yb.yff.game.service.IWarReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
 * @Class: UnionMgrImpl
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟数据管理
 */
@Component
@Slf4j
public class WarReportMgrImpl implements IJsonDataHandler {
	@Autowired
	IWarReportService warReportService;

	@Autowired
	GeneralMgrImpl generalMgr;

	@Autowired
	BusinessDataSyncImpl<GeneralDTO> generalDataSync;

	/**
	 * 同步数据到数据库
	 *
	 * @return
	 */
	@Override
	public void syncData2DB() {

	}

	/**
	 * 获取最近战报
	 *
	 * @param rid
	 * @param lastNum
	 * @return
	 */
	public List<WarReportDTO> selectWarReport(Integer rid, Integer lastNum) {

		List<WarReportDTO> reports = new ArrayList<>();

		try {
			List<WarReportEntity> entityList = ((WarReportMapper)warReportService.getBaseMapper())
					.selectWarReport(rid,lastNum);

			entityList.forEach(entity -> {
				WarReportDTO dto = wartReportEntity2DTO(entity);
				reports.add(dto);
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return reports;
	}

	/**
	 * 阅读全部战报
	 */
	@Transactional
	public boolean readWarReportAll(Integer rid) throws Exception {
		int rowsUpdated = ((WarReportMapper)warReportService.getBaseMapper()).readWarReportAll(rid);

		return rowsUpdated > 0;
	}

	/**
	 * 阅读战报
	 */
	public boolean readWarReport(Integer rid, Integer reportId) throws Exception {
		int rowsUpdated = ((WarReportMapper)warReportService.getBaseMapper()).readWarReport(rid, reportId);

		return rowsUpdated > 0;
	}

	public boolean updateWartReport2DB(WarReportDTO warReport) {
		WarReportEntity entity = wartReportDTO2Entity(warReport);

		try {
			boolean result = warReportService.save(entity);

			warReport.setId(entity.getId());

			return result;
		} catch (Exception e) {
			log.error("warReportService.save error:", e.getMessage());
		}

		return false;
	}

	private WarReportEntity wartReportDTO2Entity(WarReportDTO warReport) {
		WarReportEntity entity = new WarReportEntity();
		BeanUtils.copyProperties(warReport, entity);

		entity.setAIsRead(warReport.getAIsRead() ? 1 : 0);
		entity.setDIsRead(warReport.getDIsRead() ? 1 : 0);

		return entity;
	}

	private WarReportDTO wartReportEntity2DTO(WarReportEntity entity) {
		WarReportDTO warReport = new WarReportDTO();
		BeanUtils.copyProperties(entity, warReport);

		warReport.setAIsRead(entity.getAIsRead() == 1 ? true : false);
		warReport.setDIsRead(entity.getDIsRead() == 1 ? true : false);

		return warReport;
	}
}
