package com.yb.yff.sb.jvm.threads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
 * @Class: FutureExample
 * @CreatedOn 2024/9/23.
 * @Email: yangboyff@gmail.com
 * @Description: 多线程中Future的使用
 */
public class FutureExample {
    public static void main(String[] args) {
        FutureExample example = new FutureExample();
        example.futureExample1();
    }

    public void futureExample1() {
// 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交异步任务，返回 Future 对象
        Future<Integer> future = executor.submit(() -> {
            // 模拟耗时任务
            System.out.println("线程池线程等待5秒");
            Thread.sleep(5000);
            return 42;
        });

        try {
            // 休眠 2 秒（2000 毫秒）
            System.out.println("Main线程等待2两秒");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 主线程可以做其他事情
        System.out.println("异步任务已提交");

        try {
            // 获取任务的结果
            Integer result = future.get(3000, java.util.concurrent.TimeUnit.MILLISECONDS);  // 阻塞等待任务完成
            System.out.println("异步任务结果: " + result);
        } catch (InterruptedException | ExecutionException | java.util.concurrent.TimeoutException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();  // 关闭线程池
        }
    }

    public void futureExample2() {
    }
}
