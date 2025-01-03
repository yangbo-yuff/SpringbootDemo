package com.yb.yff.game.business.businessLogic;

import com.yb.yff.game.data.dto.city.CityDTO;
import com.yb.yff.game.data.dto.city.PositionDTO;
import com.yb.yff.game.data.dto.nationMap.config.MBCustomConfigLevelNeedDTO;
import com.yb.yff.game.data.dto.role.*;
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
 * @Class: BuildMgrImpl
 * @CreatedOn 2024/11/3.
 * @Email: yangboyff@gmail.com
 * @Description: 角色业务逻辑
 */

public interface IRoleLogic {


	/**
	 * 按资源产量的 倍数采集资源
	 *
	 * @param multiple
	 */
	void roleCollect(Integer multiple);


	/**
	 * 角色登录
	 *
	 * @param sessionStr
	 * @param enterServerResDTO
	 */
	ResponseCode roleEnterServer(String sessionStr, EnterServerResDTO enterServerResDTO);

	/**
	 * 创建角色
	 *
	 * @param createDTO
	 * @param roleResDTO
	 */
	void createRole(CreateDTO createDTO, RoleResDTO roleResDTO);

	/**
	 * 整合角色属性
	 *
	 * @param myPropertyResDTO
	 */
	ResponseCode integrateRoleProperty(Integer rid, MyPropertyResDTO myPropertyResDTO);

	/**
	 * 获取角色标记位置
	 *
	 * @return
	 */
	List<PosTagDTO> getRolePosTags(Integer rid);

	/**
	 * 更新角色中心位置
	 *
	 * @param rid
	 * @param newPosition
	 */
	void upRoleCenterPos(Integer rid, PositionDTO newPosition);

	/**
	 * 获取用户角色
	 *
	 * @param uid
	 * @return
	 */
	List<RoleDTO> getUserRole(Integer uid);

	/**
	 * 获取用户角色
	 *
	 * @param rid
	 * @return
	 */
	RoleDTO getRole(Integer rid);

	/**
	 * 获取角色主城信息
	 *
	 * @param rid
	 * @return
	 */
	List<CityDTO> getRoleCitys(Integer rid);

	/**
	 * 获取角色资源
	 *
	 * @param rid
	 * @return
	 */
	RoleResourceData getRoleResource(Integer rid);

	/**
	 * 处理标记
	 *
	 * @param rid
	 * @param posTagTypeDTO
	 */
	ResponseCode handlePosTag(Integer rid, PosTagTypeDTO posTagTypeDTO);

	/**
	 * 获取用户资源
	 *
	 * @param rid
	 * @param roleResource
	 * @param isIn
	 * @return
	 */
	boolean updateRoleResource(Integer rid, RoleResourceDTO roleResource, boolean isIn);

	/**
	 * 检查资源是否足够
	 *
	 * @param rid
	 * @param need
	 * @return
	 */
	ResponseCode checkUserNeed(Integer rid, MBCustomConfigLevelNeedDTO need);
}
