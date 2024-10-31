package com.yb.yff.sb.data.dto.general;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
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
 * @Class: MyGenerals
 * @CreatedOn 2024/10/21.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：武将列表
 */
@Data
public class GeneralsResDTO extends GameBusinessResBaseDTO {
	List<GeneralDTO>  generals;
}
