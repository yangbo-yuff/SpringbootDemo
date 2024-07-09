package com.yb.yff.sb.kafka.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

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
 * @Class: MyKafkaListenerData
 * @CreatedOn 2024/7/2.
 * @Email: yangboyff@gmail.com
 * @Description: 监听器数据
 */

@Data
@Validated
public class MyKafkaListenerData {
    @NotEmpty(message = "topic不能为空")
    private String topic;

    @NotEmpty(message = "groupId不能为空")
    private String groupId;
}
