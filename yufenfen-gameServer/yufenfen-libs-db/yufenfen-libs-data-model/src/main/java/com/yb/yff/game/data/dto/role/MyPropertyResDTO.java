package com.yb.yff.game.data.dto.role;

import com.yb.yff.game.data.dto.city.BuildDTO;
import com.yb.yff.game.data.dto.general.GeneralDTO;
import com.yb.yff.sb.data.dto.GameBusinessResBaseDTO;
import com.yb.yff.game.data.dto.army.ArmyDTO;
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
 * @Class: MyPropertyResDTO
 * @CreatedOn 2024/10/22.
 * @Email: yangboyff@gmail.com
 * @Description: 业务：我的财产
 */
@Data
public class MyPropertyResDTO extends GameBusinessResBaseDTO {
 private List<ArmyDTO> armys;
 private List<CityDTO> citys;
 private List<GeneralDTO> generals;
 private List<BuildDTO> mrBuilds;
 private RoleResourceData roleRes;
}
