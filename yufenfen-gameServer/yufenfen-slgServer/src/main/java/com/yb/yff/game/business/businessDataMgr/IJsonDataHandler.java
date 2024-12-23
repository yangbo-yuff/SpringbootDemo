package com.yb.yff.game.business.businessDataMgr;

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
 * @Class: IJsonDataHandler
 * @CreatedOn 2024/11/21.
 * @Email: yangboyff@gmail.com
 * @Description: Json配置数据处理
 */
public interface IJsonDataHandler {
 /**
  * 同步数据到DB
   */
 void syncData2DB();
}
