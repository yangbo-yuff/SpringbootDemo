package com.yb.yff.game.data.dto.ws;

import com.yb.yff.game.data.dto.UserInfoDTO;
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
 * @Class: MessageUserInfo
 * @CreatedOn 2024/10/11.
 * @Email: yangboyff@gmail.com
 * @Description: 用户信息
 */
@Data
public class MessageUserInfo extends UserInfoDTO {
 /**
  * 用户ID
  */
 private Integer uid;
}
