package com.yb.yff.sb.jvm.threads;

import java.util.Vector;

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
 * @Class: VectorDemo
 * @CreatedOn 2024/9/2.
 * @Email: yangboyff@gmail.com
 * @Description:
 */
public class VectorTest {
    public static void main(String[] args) {
        VectorTest vectorTest = new VectorTest();

        vectorTest.demo1();
    }

    private Vector<Integer> vector = new Vector<Integer>();

    public void demo1(){
        for (int i = 0; i < 10000; i++){
            vector.add(i);
        }

        Thread removeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 160; i < vector.size(); i++){
                    System.out.println("removeThread ======== "  + i);
                    vector.remove(i);
                }
            }
        });

        Thread printThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < vector.size(); i++){
                    System.out.println("printThread ======== "  + vector.get(i));
                }


                System.out.println("printThread ======== "  + vector.get(160));
                System.out.println("printThread ======== "  + vector.get(162));
                System.out.println("printThread ======== "  + vector.get(164));
            }
        });

        removeThread.start();
        printThread.start();

//        while (Thread.activeCount() > 20);
    }
}
