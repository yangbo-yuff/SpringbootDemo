package com.yb.yff.sb.jvm.heap;

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
 * @Class: MyTestIF
 * @CreatedOn 2024/9/18.
 * @Email: yangboyff@gmail.com
 * @Description: 接口测试
 */
public interface MyTestIF {
    void test();

    default void test2(){
        System.out.println("test2");
    }

    static void test3(){
        System.out.println("test3");
    }

    static int ADADFAS = 1;
}
