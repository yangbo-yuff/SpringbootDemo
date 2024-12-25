package com.yb.yff.game.data.dto.nationMap;

import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.game.data.dto.army.ArmyDTO;
import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.city.CityDTO;
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
 * @Class: ConfigResDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：国际地图扫描区域
 */
@Data
public class ScanBlockResDTO extends GameBusinessResBaseDTO {
	private List<ArmyDTO> armys;
	private List<CityDTO> mcBuilds;
	private List<BuildDTO> mrBuilds;
}
