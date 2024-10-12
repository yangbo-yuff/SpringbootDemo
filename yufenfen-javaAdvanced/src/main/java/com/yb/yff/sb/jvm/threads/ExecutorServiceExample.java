package com.yb.yff.sb.jvm.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * @Class: ExecutorServiceExample
 * @CreatedOn 2024/9/23.
 * @Email: yangboyff@gmail.com
 * @Description: ExecutorService Demo
 */
public class ExecutorServiceExample {
    public static void main(String[] args) {
        ExecutorServiceExample example = new ExecutorServiceExample();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交 5 个任务
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                example.task(taskId);
            });
        }

        // 关闭线程池
        executor.shutdown();

        try {
            executor.submit(() -> {
                example.task(100);
            });
        } catch (Exception e) {
            System.out.println("Executor is shut down, no more tasks can be submitted.");
            e.printStackTrace();
        }


        // 这不会再接受新的任务
        System.out.println("Executor is shut down, but tasks in queue will finish.");


    }

    public void task(int taskId) {
        System.out.println("Task " + taskId + " is running");
        try {
            Thread.sleep(2000); // 模拟任务执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task " + taskId + " completed");
    }
}
