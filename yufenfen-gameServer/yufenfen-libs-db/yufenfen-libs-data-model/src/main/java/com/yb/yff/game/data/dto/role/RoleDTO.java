package com.yb.yff.game.data.dto.role;

import com.yb.yff.game.data.dto.city.PositionDTO;
import lombok.Data;

import java.util.Date;

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
public class RoleDTO {
	/**
	 * role Id
	 */
	private Integer rid;

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
	private Integer sex;

	/**
	 * nick_name
	 */
	private String nickName;

	/**
	 * 个人简介
	 */
	private String profile;

	/**
	 * 余额
	 */
	private Integer balance;

	/**
	 * 木
	 */
	private Integer wood;

	/**
	 * 铁
	 */
	private Integer iron;

	/**
	 * 石头
	 */
	private Integer stone;

	/**
	 * 粮食
	 */
	private Integer grain;

	/**
	 * 金币
	 */
	private Integer gold;

	/**
	 * 令牌
	 */
	private Integer decree;

	/**
	 * 征收次数
	 */
	private Integer collectTimes;

	/**
	 * 最后征收时间
	 */
	private Date lastCollectTime;

	/**
	 * 收藏的位置
	 */
	private String posTags;

    /**
    * 上级联盟id
    */
    private Integer parentId;

	/**
	 * 中心位置
	 */
	private PositionDTO centerPos;

	/**
	 * 联盟ID
	 */
	private Integer unionId;
}
