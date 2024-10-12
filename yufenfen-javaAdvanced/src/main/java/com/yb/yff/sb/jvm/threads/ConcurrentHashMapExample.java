package com.yb.yff.sb.jvm.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: ConcurrentHashMapExample
 * @CreatedOn 2024/9/23.
 * @Email: yangboyff@gmail.com
 * @Description: ConcurrentHashMap与HashMap
 */
public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        ConcurrentHashMapExample example = new ConcurrentHashMapExample();
//        example.hashMapExample();
//        example.concurrentHashMapExample();
        example.concurrentHashMapExample2();
    }

    HashMap<String, String> hMap = new HashMap<>();

    public void hashMapExample() {
        hMap.put("key1", "value1-");
        hMap.put("key2", "value2-");
        hMap.put("key3", "value3-");
        hMap.put("key4", "value4-");

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                testSynchronized("key1", finalI);
            }).start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=========== hashMapExample：" + chMap.get("key1"));  // 输出：value1
    }

    synchronized void testSynchronized(String key, int index) {
        test(key, index);
    }

    void test(String key, int index) {
        String value = hMap.get(key);
        System.out.println(value);  // 输出：value1
        hMap.put(key, value + "-" + index);
    }

    synchronized void testCHM(String key, int index) {
        String value = chMap.get(key);
        System.out.println(value);  // 输出：value1
        chMap.put(key, value + "-" + index);
//        chMap.compute(key, (k, v) -> v + "-" + index);
    }

    ConcurrentHashMap<String, String> chMap = new ConcurrentHashMap<>();


    public void concurrentHashMapExample() {
        chMap.put("key1", "value1-");
        chMap.put("key2", "value2-");
        chMap.put("key3", "value3-");
        chMap.put("key4", "value4-");

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                testCHM("key1", finalI);
            }).start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=========== concurrentHashMapExample：" + chMap.get("key1"));  // 输出：value1
    }

    public void concurrentHashMapExample1() {
        Map<String, String> hashMap = new ConcurrentHashMap<>();

        // 创建两个线程，同时向 HashMap 写入数据
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                hashMap.put("key" + i, "value" + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 10000; i < 20000; i++) {
                hashMap.put("key" + i, "value" + i);
            }
        });

        // 启动线程
        t1.start();
        t2.start();

        try {
            // 等待线程执行完毕
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打印 map 的大小
        System.out.println("HashMap size: " + hashMap.size());
    }

    public void concurrentHashMapExample2() {
        // 切换这两行可以测试不同的Map
         HashMap<Integer, String> map = new HashMap<>();  // 非线程安全
//        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();  // 线程安全

        // 初始化一些数据
        map.put(1, "Value1");
        map.put(2, "Value2");

        // 写线程 1 - 添加和更新元素
        Thread writer1 = new Thread(() -> {
            for (int i = 3; i < 10000; i++) {
                map.put(i, "Value" + i);
            }
        });

        // 写线程 2 - 添加和更新元素
        Thread writer2 = new Thread(() -> {
            for (int i = 10000; i < 20000; i++) {
                map.put(i, "Value" + i);
            }
        });

        // 读线程 - 不断读取数据
        Thread reader = new Thread(() -> {
            for (int i = 1; i < 20000; i++) {
                System.out.println("Key: " + i + ", Value: " + map.get(i));
            }
        });

        // 启动所有线程
        writer1.start();
        writer2.start();
        reader.start();

        try {
            // 等待所有线程结束
            writer1.join();
            writer2.join();
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 输出最终map的大小
        System.out.println("Final map size: " + map.size());
    }
}

