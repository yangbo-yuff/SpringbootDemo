package com.yb.yff.game.business.businessLogic;

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
 * @Class: ISyncExecute
 * @CreatedOn 2024/12/3.
 * @Email: yangboyff@gmail.com
 * @Description: 数据同步
 */
public interface ISyncExecute<T> {
    /**
     * 数据同步
     * @param t
     */
    void syncExecute(Integer rid, T t);
}
