package com.yb.yff.sb.data.dto.union;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
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
 * @Class: CreateResDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务： 创建联盟
 */
@Data
public class CreateResDTO extends GameBusinessResBaseDTO {
 private Integer id;
 private String name;
}
