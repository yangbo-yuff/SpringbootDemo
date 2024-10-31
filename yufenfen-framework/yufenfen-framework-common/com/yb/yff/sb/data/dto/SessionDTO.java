package com.yb.yff.sb.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

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
 * @Class: SessionDTO
 * @CreatedOn 2024/10/23.
 * @Email: yangboyff@gmail.com
 * @Description: Session
 */
@Data
public class SessionDTO {
	private LocalDateTime mTime;
	private Integer id;
}
