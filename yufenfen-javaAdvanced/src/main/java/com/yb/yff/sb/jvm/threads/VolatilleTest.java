package com.yb.yff.sb.jvm.threads;

import java.util.Map;

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
 * @Project: JVMDemo
 * @Class: VolatilleTest
 * @CreatedOn 2024/8/31.
 * @Email: yangboyff@gmail.com
 * @Description: volatile关键字可见性
 */
public class VolatilleTest {

    public static void main(String[] args) {
        VolatilleTest  volatilleTest = new VolatilleTest();

        volatilleTest.volatileDemo1();
    }


    /**----------------volatileDemo1--------------------**/
    public volatile int reace = 0;

    public void increase() {
        reace++;
    }

    private final int THREADS_COUNT = 20;
    /**
     * volatile 可见性调试
     */
    public void volatileDemo1() {
        Thread[] trheads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            trheads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        increase();
                    }
                }
            });
            trheads[i].start();
        }

        while (Thread.activeCount() > 1) {
            Thread.yield();
        }


        System.out.println("-------------- Volatile Reasult reace: " + reace);
    }

    /**----------------volatileDemo2--------------------**/
    volatile boolean initialized = false;

    /**
     * volatile 可见性调试
     */
    public void volatileDemo2() {
         Map configOptions;
         char[] configTest;

          
    }
}
