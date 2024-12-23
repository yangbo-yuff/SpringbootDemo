package com.yb.yff.game.data.dto;

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
 * @Class: ClientSessionIdUidRidDTO
 * @CreatedOn 2024/12/17.
 * @Email: yangboyff@gmail.com
 * @Description: 缓存客户端SessionID 和 对应的 uid rid
 */
@Data
public class ClientSessionIdUidRidDTO {
    private String sessionId;
    private Integer uid;
    private Integer rid;

    public ClientSessionIdUidRidDTO() {
    }

    public ClientSessionIdUidRidDTO(String sessionId, Integer uid, Integer rid) {
        this.sessionId = sessionId;
        this.uid = uid;
        this.rid = rid;
    }
}
