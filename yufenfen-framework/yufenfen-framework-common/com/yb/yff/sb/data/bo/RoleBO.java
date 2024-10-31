package com.yb.yff.sb.data.bo;

import com.yb.yff.sb.data.dto.army.ArmyDTO;
import com.yb.yff.sb.data.dto.city.BuildDTO;
import com.yb.yff.sb.data.dto.city.CityDTO;
import com.yb.yff.sb.data.dto.city.FacilityDTO;
import com.yb.yff.sb.data.dto.city.PositionDTO;
import com.yb.yff.sb.data.dto.general.GeneralDTO;
import lombok.Data;

import java.util.Date;
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
 * @Class: GameRoleInfoDTO
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏角色
 */
@Data
public class RoleBO {
	private Integer id;

	/**
	 * 用户UID
	 */
	private Integer uid;

	/**
	 * 头像Id
	 */
	private Integer headId;

	/**
	 * 性别，0:女 1男
	 */
	private Byte sex;

	/**
	 * nick_name
	 */
	private String nickName;

	/**
	 * 余额
	 */
	private Integer balance;

	/**
	 * 个人简介
	 */
	private String profile;

	/**
	 * 上级联盟id
	 */
	private Byte parentId;

	/**
	 * 征收次数
	 */
	private Byte collectTimes;

	/**
	 * 最后征收时间
	 */
	private Date lastCollectTime;

	/**
	 * 收藏的位置
	 */
	private String posTags;

	/**************************************/
	/************ 其它附带信息 **************/
	/**************************************/

	/**
	 * 中心位置
	 */
	private PositionDTO centerPos;

	/**
	 * 主城
	 */
	private List<CityDTO> cityList;


	/**
	 * 主城设施
	 * <cityId, List<FacilitiesDTO>
	 */
	private Map<Integer, List<FacilityDTO>> cityFacilities;

	/**
	 * 建筑
	 */
	private List<BuildDTO> buildList;

	/**
	 * 军队
	 */
	private List<ArmyDTO> armyList;

	/**
	 * 武将
	 */
	private List<GeneralDTO> generalDTOList;
}
