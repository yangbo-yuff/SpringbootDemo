package com.yb.yff.game.service.DelayedTask;

import com.yb.yff.sb.data.dto.GameMessageEnhancedResDTO;

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
 * @Class: IDelayedTaskListener
 * @CreatedOn 2024/10/30.
 * @Email: yangboyff@gmail.com
 * @Description: 延时任务执行监听者
 */
public interface IDelayedTaskListener {
 /**
  * 延时任务执行完成
  * @param seesionId
  * @param takskId
  * @param result
  */
 void onDelayedTaskFinish(String seesionId, String takskId, GameMessageEnhancedResDTO result);
}
