package com.yb.yff.game.data.dto.army;

import lombok.Data;

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
 * @Class: conscriptDTO
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 征兵
 */
@Data
public class ConscriptDTO {
 /**
  * 军队ID
  */
 private Integer armyId;

 /**
  * 征兵数量
  */
 private List<Integer> cnts;
}
