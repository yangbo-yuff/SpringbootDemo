package com.yb.yff.sb.jvm.classloader;

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
 * @Class: loaderTest
 * @CreatedOn 2024/9/3.
 * @Email: yangboyff@gmail.com
 * @Description: 类加载器Demo
 */

public class LoaderTest {
    public static void main(String[] args) {
        loaderDemo1();
//        loaderDemo2();
    }

    public static void loaderDemo1() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(loader);
        System.out.println(loader.getParent());
        System.out.println(loader.getParent().getParent());
    }

    public static void loaderDemo2() {

        try {
            //使用ClassLoader.loadClass()来加载类，不会执行初始化块
//            ClassLoader loader = LoaderTest.class.getClassLoader();
//            loader.loadClass("Test2");
            //使用Class.forName()来加载类，默认会执行初始化块
//            Class.forName("Test2");
            Test2.class.getClassLoader().loadClass("Test2");

            //使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块
//                Class.forName("Test2", false, loader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}



