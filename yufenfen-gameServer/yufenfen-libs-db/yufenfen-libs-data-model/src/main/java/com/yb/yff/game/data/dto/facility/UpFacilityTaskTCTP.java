package com.yb.yff.game.data.dto.facility;

import com.yb.yff.game.data.dto.facility.config.FacilityPropertyLevel;
import com.yb.yff.sb.taskCallback.TimeConsumingTaskParam;
import lombok.Data;

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
 * @Class: UpFacilityTaskTCTP
 * @CreatedOn 2024/11/4.
 * @Email: yangboyff@gmail.com
 * @Description: 设施升级延时任务回调参数
 */
@Data
public class UpFacilityTaskTCTP extends TimeConsumingTaskParam {
    private Integer cityId;
    private Integer fType;
    private FacilityPropertyLevel facilityPropertyLevel;
}
